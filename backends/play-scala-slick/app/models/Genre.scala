package models

import play.api.libs.json.Json

case class Genre(
                  name: String,
                  description: String
                )

object Genre {
  implicit val jsonFormat = Json.format[Genre]
}