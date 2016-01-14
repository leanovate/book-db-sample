package handlers

import models.ErrorMessage
import play.api.http.HttpErrorHandler
import play.api.libs.json.Json
import play.api.mvc.{RequestHeader, Result}
import play.api.mvc.Results._

import scala.concurrent.Future

class ErrorHandler extends HttpErrorHandler {
  override def onClientError(request: RequestHeader, statusCode: Int, message: String): Future[Result] =
    Future.successful(
      Status(statusCode)(Json.toJson(ErrorMessage(statusCode, message)))
    )

  override def onServerError(request: RequestHeader, exception: Throwable): Future[Result] =
    Future.successful(
      InternalServerError(Json.toJson(ErrorMessage(500, exception.getMessage)))
    )
}
