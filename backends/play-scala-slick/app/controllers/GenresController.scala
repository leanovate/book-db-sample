package controllers

import javax.inject.Inject

import dao.GenreDAO
import models.{ErrorMessage, Genre}
import play.api.libs.json.{JsError, JsSuccess, Json}
import play.api.mvc.{Action, Controller}

import scala.concurrent.{ExecutionContext, Future}

class GenresController @Inject()(genreDao: GenreDAO)(implicit executionContext: ExecutionContext) extends Controller {
  def listAll(offset: Int, limit: Int) = Action.async {
    request =>
      genreDao.all(offset, limit).map {
        genrePage =>
          Ok(Json.toJson(genrePage))
      }
  }

  def create = Action.async(parse.json) {
    request =>
      request.body.validate[Genre] match {
        case JsSuccess(genre, _) =>
          genreDao.insert(genre).map(_ => Created)
        case JsError(errors) =>
          Future.successful(BadRequest(Json.toJson(ErrorMessage(400, "Invalid json").withJsErrors(errors))))
      }
  }

  def findById(name: String) = Action.async {
    request =>
      genreDao.findById(name).map {
        case Some(genre) => Ok(Json.toJson(genre))
        case None => NotFound(Json.toJson(ErrorMessage(404, s"No genre with name $name")))
      }
  }

  def delete(name: String) = Action.async {
    request =>
      genreDao.delete(name).map {
        _ => NoContent
      }
  }
}
