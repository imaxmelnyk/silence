package com.imaxmelnyk.silence

import java.io.File
import java.time.Duration

import scopt.{OParser, OParserBuilder}

case class Config(inputFile: File = new File(""),
                  outputFile: File = new File(""),
                  chapterBreak: Duration = Duration.ZERO,
                  chapterPartBreak: Duration = Duration.ZERO,
                  maxChapterDuration: Duration = Duration.ZERO)

object Config {
  private val builder: OParserBuilder[Config] = OParser.builder[Config]
  private val parser = {
    import builder._

    OParser.sequence(
      programName("silence"),
      opt[String]("input-file")
        .required()
        .valueName("<path/to/file>")
        .action((o, c) => c.copy(inputFile = new File(o)))
        .validate { inputFilePath =>
          val inputFile = new File(inputFilePath)

          if (inputFile.exists && inputFile.isFile) success
          else failure("input-file is not a file or does not exists")
        }
        .text("input-file is required property represents an xml file with silences"),
      opt[String]("output-file")
        .required()
        .valueName("<path/to/file>")
        .action((o, c) => c.copy(outputFile = new File(o)))
        .text("output-file is required property represents a json file where segments will be stored"),
      opt[String]("chapter-break")
        .required()
        .valueName("<duration string>")
        .action((o, c) => c.copy(chapterBreak = Duration.parse(o)))
        .text("chapter-break is required string property represents minimum silence duration to split into chapters"),
      opt[String]("chapter-part-break")
        .required()
        .valueName("<duration string>")
        .action((o, c) => c.copy(chapterPartBreak = Duration.parse(o)))
        .text("chapter-part-break is required string property represents minimum silence duration to split chapters into parts"),
      opt[String]("max-chapter-duration")
        .required()
        .valueName("<duration string>")
        .action((o, c) => c.copy(maxChapterDuration = Duration.parse(o)))
        .text("max-chapter-duration is required string property represents maximum chapter duration without splitting it"))
  }

  def parse(args: Seq[String]): Either[String, Config] = {
    OParser.parse(parser, args, Config()) match {
      case Some(config) => Right(config)
      case _ => Left("ParsingConfigError")
    }
  }
}