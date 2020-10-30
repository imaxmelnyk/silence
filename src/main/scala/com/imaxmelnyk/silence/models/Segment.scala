package com.imaxmelnyk.silence.models

import java.time.Duration

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class Segment(title: String,
                   offset: Duration)

object Segment {
  implicit val encoder: Encoder[Segment] = deriveEncoder[Segment]
  implicit val decoder: Decoder[Segment] = deriveDecoder[Segment]
}
