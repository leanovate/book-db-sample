package controllers

import java.util.UUID
import javax.inject.Inject

import dao.{BooksDAO, GenreDAO, AuthorDAO}
import models.{Book, ErrorMessage, Author}
import play.api.http.HeaderNames
import play.api.libs.json.{JsError, JsSuccess, Json}
import play.api.mvc.{Action, Controller}

import scala.concurrent.{Future, ExecutionContext}

class BooksController @Inject()(authorDao: AuthorDAO,
                                genresDao: GenreDAO,
                                booksDao: BooksDAO)(implicit executionContext: ExecutionContext) extends Controller{
  def listAll(offset: Int, limit: Int) = Action.async {
    request =>
      booksDao.all(offset, limit).map {
        booksPage =>
          Ok(Json.toJson(booksPage))
      }
  }

  def create = Action.async(parse.json) {
    implicit request =>
      request.body.validate[Book] match {
        case JsSuccess(book, _) =>
          booksDao.insert(book).map {
            _ => Created.withHeaders(HeaderNames.LOCATION -> routes.BooksController.findById(book.id).absoluteURL)
          }
        case JsError(errors) =>
          Future.successful(BadRequest(Json.toJson(ErrorMessage(400, "Invalid json").withJsErrors(errors))))
      }
  }

  def findById(id: UUID) = Action.async {
    request =>
      booksDao.findById(id).map {
        case Some(book) => Ok(Json.toJson(book))
        case None => NotFound(Json.toJson(ErrorMessage(404, s"No book with id $id")))
      }
  }

}
