package com.imaxmelnyk.silence.models

import java.time.Duration

import com.lucidchart.open.xtract.XmlReader._
import com.lucidchart.open.xtract.{XmlReader, __}

case class Silence(from: Duration,
                   until: Duration) {
  lazy val duration: Duration = until.minus(from)
}

object Silence {
  implicit val silenceXmlReader: XmlReader[Silence] = {
    for {
      from <- attribute[String]("from")
      until <- attribute[String]("until")
    } yield {
      Silence(Duration.parse(from), Duration.parse(until))
    }
  }

  implicit val silenceSeqXmlReader: XmlReader[Seq[Silence]] = (__ \ "silence").read(seq[Silence])
}
