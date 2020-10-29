package com.imaxmelnyk.silence.models

import java.time.Duration

import io.circe.Encoder
import io.circe.generic.semiauto.deriveEncoder

case class Segment(title: String,
                   offset: Duration)

object Segment {
  implicit val encoder: Encoder[Segment] = deriveEncoder[Segment]
}
