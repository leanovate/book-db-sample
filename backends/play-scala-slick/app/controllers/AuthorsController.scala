package controllers

import java.util.UUID
import javax.inject.Inject

import dao.AuthorDAO
import models.{Author, ErrorMessage}
import play.api.http.HeaderNames
import play.api.libs.json.{JsError, JsSuccess, Json}
import play.api.mvc.{Action, Controller}

import scala.concurrent.{ExecutionContext, Future}

class AuthorsController @Inject()(authorDao: AuthorDAO)(implicit executionContext: ExecutionContext) extends Controller {
  def create = Action.async(parse.json) {
    request =>
      request.body.validate[Author] match {
        case JsSuccess(author, _) =>
          authorDao.insert(author).map {
            _ => Created.withHeaders(HeaderNames.LOCATION -> routes.AuthorsController.findById(author.id).absoluteURL)
          }
        case JsError(errors) =>
          Future.successful(BadRequest(Json.toJson(ErrorMessage(400, "Invalid json").withJsErrors(errors))))
      }
  }


  def findById(id: UUID) = Action.async {
    request =>
      authorDao.findById(id).map {
        case Some(author) => Ok(Json.toJson(author))
        case None => NotFound(Json.toJson(ErrorMessage(404, s"No author with name $id")))
      }
  }
}
