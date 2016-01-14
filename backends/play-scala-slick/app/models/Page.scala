package models

import play.api.libs.functional.syntax._
import play.api.libs.json._

case class Page[T](
                    offset: Int,
                    limit: Int,
                    total: Int,
                    entries: Seq[T]
                  )

object Page {
  implicit def jsonWrites[T](implicit entriesWrites: Writes[T]) : Writes[Page[T]] = (
    (JsPath \ "offset").write[Int] and
      (JsPath \ "limit").write[Int] and
      (JsPath \ "total").write[Int] and
      (JsPath \ "entries").write[Seq[T]]
    )(unlift(Page.unapply[T]))
}