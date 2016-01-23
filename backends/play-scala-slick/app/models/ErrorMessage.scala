package models

import play.api.data.validation.ValidationError
import play.api.libs.json.{JsPath, Json}

case class ErrorDetail(
                        in: Option[String],
                        position: Option[String],
                        message: String
                      )

object ErrorDetail {
  implicit val jsonFormat = Json.format[ErrorDetail]
}

case class ErrorMessage(
                         code: Int,
                         message: String,
                         details: Option[Seq[ErrorDetail]] = None
                       ) {
  def withJsErrors(errors: Seq[(JsPath, Seq[ValidationError])]): ErrorMessage =
    copy(details = Some(errors.map {
      case (path, validationErrors) =>
        ErrorDetail(
          in = Some("json"),
          position = Some(path.toString()),
          message = validationErrors.map(_.message).mkString(", "))
    }))
}

object ErrorMessage {
  implicit val jsonFormat = Json.format[ErrorMessage]
}
