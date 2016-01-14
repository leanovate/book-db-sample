package models

import play.api.data.validation.ValidationError
import play.api.libs.json.{JsPath, Json}

case class ErrorMessage(
                         code: Int,
                         message: String,
                         details: Option[Seq[String]] = None
                       ) {
  def withJsErrors(errors: Seq[(JsPath, Seq[ValidationError])]): ErrorMessage =
    copy(details = Some(errors.map {
      case (path, errors) =>
        s"$path: ${errors.map(_.message).mkString(", ")}"
    }))
}

object ErrorMessage {
  implicit val jsonFormat = Json.format[ErrorMessage]
}
