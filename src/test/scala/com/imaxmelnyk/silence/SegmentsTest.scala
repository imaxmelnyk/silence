package com.imaxmelnyk.silence

import java.time.Duration

import com.imaxmelnyk.silence.models.{Segments, Silence}
import com.lucidchart.open.xtract.XmlReader
import org.scalatest.flatspec.AnyFlatSpecLike
import org.scalatest.matchers.should.Matchers
import io.circe.parser._

import scala.xml.XML

class SegmentsTest extends AnyFlatSpecLike with Matchers {
  implicit val config: Config = Config(
    chapterBreak = Duration.ofSeconds(3),
    chapterPartBreak = Duration.ofSeconds(1),
    maxChapterDuration = Duration.ofMinutes(10))

  "Segments" should "be computed correctly" in {
    val silencesXml = XML.load(getClass.getResourceAsStream("/silences.xml"))
    val silences = XmlReader.of[Seq[Silence]].read(silencesXml).getOrElse(Seq())
    val computedSegments = Segments(silences)

    val segmentsJson = parse(new String(getClass.getResourceAsStream("/segments.json").readAllBytes)).right.get
    val segments = segmentsJson.as[Segments].right.get

    computedSegments shouldEqual segments
  }
}
