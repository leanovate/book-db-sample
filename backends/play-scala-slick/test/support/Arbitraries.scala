package support

import models.{Genre, Author, ErrorMessage}
import org.scalacheck.{Arbitrary, Gen}

trait Arbitraries {
  implicit val arbitraryAuthor = Arbitrary[Author](for {
    id <- Gen.uuid
    name <- Arbitrary.arbitrary[String]
  } yield Author(id, name))

  implicit val arbitraryGenre = Arbitrary[Genre](for {
    name <- Gen.identifier
    description <- Gen.option(Arbitrary.arbitrary[String])
  } yield Genre(name, description))

  implicit val arbitraryErrorMessage = Arbitrary[ErrorMessage](for {
    code <- Gen.choose(100, 599)
    message <- Arbitrary.arbitrary[String]
    details <- Gen.option(Arbitrary.arbitrary[Seq[String]])
  } yield ErrorMessage(code, message))

}
