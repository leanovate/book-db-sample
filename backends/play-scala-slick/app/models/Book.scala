package models

import java.util.UUID
import play.api.libs.functional.syntax._
import play.api.libs.json._

case class Book(
                 id: UUID,
                 title: String,
                 genre: Option[String],
                 summary: Option[String],
                 isbn: Option[String],
                 authors: Seq[Author]
               )

object Book {
  implicit val jsonWrites = Json.writes[Book]

  implicit val jsonReads: Reads[Book] =
    ((JsPath \ "id").readNullable[UUID].map(_.getOrElse(UUID.randomUUID())) and
      (JsPath \ "title").read[String] and
      (JsPath \ "genre").readNullable[String] and
      (JsPath \ "summary").readNullable[String] and
      (JsPath \ "jsbn").readNullable[String] and
      (JsPath \ "authors").readNullable[Seq[Author]].map(_.getOrElse(Seq.empty))) (Book.apply _)
}