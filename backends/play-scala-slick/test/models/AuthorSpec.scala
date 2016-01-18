package models

import de.leanovate.swaggercheck.schema.model.ValidationResult
import de.leanovate.swaggercheck.shrinkable.CheckJsValue
import org.scalacheck.Arbitrary
import org.specs2.ScalaCheck
import org.specs2.matcher.MustMatchers
import org.specs2.mutable.Specification
import play.api.libs.json.Json
import support.{Arbitraries, BookDbApi}

class AuthorSpec extends Specification with ScalaCheck with MustMatchers with Arbitraries with BookDbApi {
  "Author" should {
    "be receivable" in {
      implicit val arbitraryJson = Arbitrary[CheckJsValue](swaggerCheck.jsonGenerator("Author"))

      prop {
        json: CheckJsValue =>
          val result = Json.parse(json.minified).validate[Author]

          result.isSuccess must beTrue
      }
    }

    "be sendable" in {
      val verifier = swaggerCheck.jsonVerifier("Author")

      prop {
        author: Author =>
          verifier.verify(Json.stringify(Json.toJson(author))) must be equalTo ValidationResult.success
      }
    }

  }
}
