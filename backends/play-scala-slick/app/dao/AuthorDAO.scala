package dao

import java.util.UUID
import javax.inject.Inject

import models.{Author, Page}
import play.api.Play
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfig}
import slick.driver.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

class AuthorDAO @Inject()()(implicit executionContext: ExecutionContext) extends HasDatabaseConfig[JdbcProfile] {
  override protected val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)

  import driver.api._

  val Authors = TableQuery[AuthorTable]

  def all(offset: Int, limit: Int): Future[Page[Author]] =
    db.run(
      for {
        total <- Authors.length.result
        authors <- Authors.sortBy(_.name).drop(offset).take(limit).result
      } yield Page(offset, limit, total, authors)
    )

  def findById(id: UUID): Future[Option[Author]] = db.run(Authors.filter(_.id === id.bind).result.headOption)

  def insert(author: Author): Future[Unit] = db.run(Authors += author).map(_ => ())

  def delete(id: UUID): Future[Unit] = db.run(Authors.filter(_.id === id.bind).delete).map(_ => ())

  class AuthorTable(tag: Tag) extends Table[Author](tag, "AUTHOR") {
    def id = column[UUID]("ID", O.PrimaryKey)

    def name = column[String]("NAME")

    def description = column[String]("DESCRIPTION").?

    def * = (id, name, description) <>((Author.apply _).tupled, Author.unapply _)
  }

}
