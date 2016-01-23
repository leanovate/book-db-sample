package support

import models._
import org.scalacheck.{Arbitrary, Gen}

trait Arbitraries {
  implicit val arbitraryAuthor = Arbitrary[Author](for {
    id <- Gen.uuid
    name <- Arbitrary.arbitrary[String]
    description <- Arbitrary.arbitrary[Option[String]]
  } yield Author(id, name, description))

  implicit val arbitraryAuthorPage = Arbitrary[Page[Author]](for {
    total <- Gen.choose(0, 20)
    offset <- Gen.choose(0, total)
    limit <- Gen.choose(5, 10)
    entries <- Gen.listOfN(limit, Arbitrary.arbitrary[Author])
  } yield Page[Author](offset, limit, total, entries))

  implicit val arbitraryGenre = Arbitrary[Genre](for {
    name <- Gen.identifier
    description <- Gen.option(Arbitrary.arbitrary[String])
  } yield Genre(name, description))

  implicit val arbitraryGenrePage = Arbitrary[Page[Genre]](for {
    total <- Gen.choose(0, 20)
    offset <- Gen.choose(0, total)
    limit <- Gen.choose(5, 10)
    entries <- Gen.listOfN(limit, Arbitrary.arbitrary[Genre])
  } yield Page[Genre](offset, limit, total, entries))

  implicit val arbitraryErrorDetail = Arbitrary[ErrorDetail](for {
    in <- Gen.option(Gen.identifier)
    position <- Gen.option(Gen.identifier)
    message <- Arbitrary.arbitrary[String]
  } yield ErrorDetail(in, position, message)

  )
  implicit val arbitraryErrorMessage = Arbitrary[ErrorMessage](for {
    code <- Gen.choose(100, 599)
    message <- Arbitrary.arbitrary[String]
    details <- Gen.option(Arbitrary.arbitrary[Seq[ErrorDetail]])
  } yield ErrorMessage(code, message, details))
}
