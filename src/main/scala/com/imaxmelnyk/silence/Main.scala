package com.imaxmelnyk.silence

import java.io.FileOutputStream

import com.imaxmelnyk.silence.models.{Segments, Silence}
import com.imaxmelnyk.silence.models.Segments.encoder
import com.lucidchart.open.xtract.XmlReader
import com.typesafe.scalalogging.LazyLogging
import io.circe.syntax._

import scala.xml.XML

object Main extends App with LazyLogging {
  val result = Config.parse(args).map { implicit config =>
    val silencesXml = XML.loadFile(config.inputFile)
    val silences = XmlReader.of[Seq[Silence]].read(silencesXml).getOrElse(Seq())
    val segments = Segments(silences)

    val outputStream = new FileOutputStream(config.outputFile)
    outputStream.write(segments.asJson.toString.getBytes)
    outputStream.close()
  }

  result.fold(logger.error(_), identity)
}
