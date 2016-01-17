package models

import java.util.UUID
import play.api.libs.json.Json

case class BookPageEntry(
                         id: UUID,
                         title: String,
                         genre: Option[String],
                         isbn: Option[String]
                       )

object BookPageEntry {
    implicit val jsonWrites = Json.writes[BookPageEntry]
}
