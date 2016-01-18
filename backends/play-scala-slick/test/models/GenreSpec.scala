package models

import de.leanovate.swaggercheck.schema.model.ValidationResult
import de.leanovate.swaggercheck.shrinkable.CheckJsValue
import org.scalacheck.Arbitrary
import org.specs2.ScalaCheck
import org.specs2.matcher.MustMatchers
import org.specs2.mutable.Specification
import play.api.libs.json.Json
import support.{BookDbApi, Arbitraries}

class GenreSpec extends Specification with ScalaCheck with MustMatchers with Arbitraries with BookDbApi {
  "Author" should {
    "be receivable" in {
      implicit val arbitraryJson = Arbitrary[CheckJsValue](swaggerCheck.jsonGenerator("Genre"))

      prop {
        json: CheckJsValue =>
          val result = Json.parse(json.minified).validate[Genre]

          result.isSuccess must beTrue
      }
    }

    "be sendable" in {
      val verifier = swaggerCheck.jsonVerifier("Genre")

      prop {
        author: Genre =>
          verifier.verify(Json.stringify(Json.toJson(author))) must be equalTo ValidationResult.success
      }
    }
  }
}
