package dao

import java.util.UUID
import javax.inject.Inject

import models.{Book, BookPageEntry, Page}
import play.api.Play
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfig}
import slick.driver.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

class BooksDAO @Inject()(authorDAO: AuthorDAO)(implicit executionContext: ExecutionContext) extends HasDatabaseConfig[JdbcProfile] {
  override protected val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)

  import driver.api._

  val Books = TableQuery[BookTable]

  val BookAuthors = TableQuery[BookAuthorTable]

  def all(offset: Int, limit: Int): Future[Page[BookPageEntry]] =
    db.run(
      for {
        total <- Books.length.result
        books <- Books.sortBy(_.title).drop(offset).take(limit).map(row => (row.id, row.title, row.isbn, row.genre)).result
      } yield Page(offset, limit, total, books.map((BookPageEntry.apply _).tupled))
    )

  def findById(id: UUID): Future[Option[Book]] =
    db.run(
      for {
        bookOption <- Books.filter(_.id === id.bind).result.headOption
        authors <- BookAuthors.filter(_.bookId === id.bind).join(authorDAO.Authors).on(_.authorId === _.id).map(_._2).result
      } yield bookOption.map {
        book =>
          Book(book.id, book.title, book.genre, book.summary, book.isbn, authors)
      }
    )

  def insert(book: Book): Future[Unit] =
    db.run(
      for {
        _ <- Books += BookRow(book.id, book.title, book.summary, book.isbn, book.genre)
        _ <- BookAuthors ++= book.authors.map(author => BookAuthorRow(book.id, author.id))
      } yield ()
    )

  def delete(id: UUID): Future[Unit] =
    db.run(
      for {
        _ <- BookAuthors.filter(_.bookId === id.bind).delete
        _ <- Books.filter(_.id === id.bind).delete
      } yield ()
    )


  case class BookRow(
                      id: UUID,
                      title: String,
                      summary: Option[String],
                      isbn: Option[String],
                      genre: Option[String]
                    )

  class BookTable(tag: Tag) extends Table[BookRow](tag, "BOOK") {
    def id = column[UUID]("ID", O.PrimaryKey)

    def title = column[String]("TITLE")

    def summary = column[String]("SUMMARY").?

    def isbn = column[String]("ISBN").?

    def genre = column[String]("GENRE").?

    def * = (id, title, summary, isbn, genre) <>(BookRow.tupled, BookRow.unapply _)
  }

  case class BookAuthorRow(
                            bookId: UUID,
                            authorId: UUID
                          )

  class BookAuthorTable(tag: Tag) extends Table[BookAuthorRow](tag, "BOOK_AUTHOR") {
    def bookId = column[UUID]("BOOK_ID", O.PrimaryKey)

    def authorId = column[UUID]("AUTHOR_ID")

    def * = (bookId, authorId) <>(BookAuthorRow.tupled, BookAuthorRow.unapply _)
  }

}
