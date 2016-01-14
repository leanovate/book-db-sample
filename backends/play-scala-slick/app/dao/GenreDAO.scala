package dao

import models.{Genre, Page}
import play.api.Play
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfig}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import slick.driver.JdbcProfile

import scala.concurrent.Future

class GenreDAO extends HasDatabaseConfig[JdbcProfile] {
  override protected val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)

  import driver.api._

  private val Genres = TableQuery[GenreTable]

  def all(offset: Int, limit: Int): Future[Page[Genre]] =
    for {
      total <- db.run(Genres.length.result)
      genres <- db.run(Genres.sortBy(_.name).drop(offset).take(limit).result)
    } yield Page(offset, limit, total, genres)

  def findById(name:String): Future[Option[Genre]] = db.run(Genres.filter(_.name === name).result.headOption)

  def insert(genre: Genre): Future[Unit] = db.run(Genres += genre).map(_ => ())

  def delete(name: String): Future[Unit] = db.run(Genres.filter(_.name === name).delete).map(_ => ())

  private class GenreTable(tag: Tag) extends Table[Genre](tag, "GENRE") {
    def name = column[String]("NAME", O.PrimaryKey)

    def description = column[String]("DESCRIPTION")

    def * = (name, description) <>((Genre.apply _).tupled, Genre.unapply _)
  }

}
