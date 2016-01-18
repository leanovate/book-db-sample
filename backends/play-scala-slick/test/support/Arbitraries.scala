package support

import models.ErrorMessage
import org.scalacheck.{Arbitrary, Gen}

trait Arbitraries {
  implicit val arbitraryErrorMessage = Arbitrary[ErrorMessage](for {
    code <- Gen.choose(100, 599)
    message <- Arbitrary.arbitrary[String]
    details <- Gen.option(Arbitrary.arbitrary[Seq[String]])
  } yield ErrorMessage(code, message))

}
