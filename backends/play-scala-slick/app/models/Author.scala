package models

import java.util.UUID
import play.api.libs.functional.syntax._
import play.api.libs.json._

case class Author(
                   id: UUID,
                   name: String,
                   description: Option[String]
                 )

object Author {
  implicit val jsonWrites = Json.writes[Author]

  implicit val jsonReads : Reads[Author] =
    ((JsPath \ "id").readNullable[UUID].map(_.getOrElse(UUID.randomUUID())) and
      (JsPath \ "name").read[String] and
      (JsPath \ "description").readNullable[String])(Author.apply _)

}
