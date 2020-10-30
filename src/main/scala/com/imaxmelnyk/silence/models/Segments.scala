package com.imaxmelnyk.silence.models

import java.time.Duration

import com.imaxmelnyk.silence.Config
import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class Segments(segments: Seq[Segment])

object Segments {
  implicit val encoder: Encoder[Segments] = deriveEncoder[Segments]
  implicit val decoder: Decoder[Segments] = deriveDecoder[Segments]

  def apply(silences: Seq[Silence])
           (implicit config: Config): Segments = {
    // initial zero silence
    val zeroSilence: Silence = Silence(Duration.ZERO, Duration.ZERO)

    // reverse ordering
    val chapterSilences: Seq[Seq[Silence]] = silences.foldLeft(Seq(Seq(zeroSilence))) {
      case (head +: tail, silence) if silence.duration.compareTo(config.chapterBreak) < 0 =>
        (silence +: head) +: tail
      case (head +: tail, silence) =>
        Seq(silence) +: ((silence +: head) +: tail)
    }

    val (segments, _): (Seq[Segment], Int) = chapterSilences.foldLeft((Seq.empty[Segment], chapterSilences.length)) {
      // always break last chapter
      case ((segments, chapterNum), chapterSilences) if segments.isEmpty && chapterSilences.length > 1 =>
        (splitChapter(segments, chapterSilences, chapterNum), chapterNum - 1)
      case ((segments, chapterNum), chapterSilences) if needsBreak(chapterSilences) && chapterSilences.length > 2 =>
        (splitChapter(segments, chapterSilences.tail, chapterNum), chapterNum - 1)
      case ((segments, chapterNum), chapterSilences) =>
        (Segment(s"Chapter $chapterNum", chapterSilences.last.until) +: segments, chapterNum - 1)
    }

    Segments(segments)
  }

  private def splitChapter(currentSegments: Seq[Segment],
                           chapterSilences: Seq[Silence],
                           chapterNum: Int): Seq[Segment] = {
    chapterSilences.foldLeft((currentSegments, chapterSilences.length)) { case ((segments, partNum), silence) =>
      (Segment(s"Chapter $chapterNum, part $partNum", silence.until) +: segments, partNum - 1)
    }._1
  }

  private def needsBreak(chapterSilences: Seq[Silence])
                        (implicit config: Config): Boolean = {
    // reverse ordering
    // chapter duration is the time between first and last silence
    chapterSilences.head.from.minus(chapterSilences.last.until).compareTo(config.maxChapterDuration) >= 0
  }
}