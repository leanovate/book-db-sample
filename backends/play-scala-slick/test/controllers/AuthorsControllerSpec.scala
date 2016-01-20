package controllers

import java.util.UUID

import dao.AuthorDAO
import de.leanovate.swaggercheck.playhelper._
import de.leanovate.swaggercheck.schema.model.ValidationResult
import models.{Author, Page}
import org.scalacheck.Arbitrary
import org.specs2.ScalaCheck
import org.specs2.mock.Mockito
import play.api.Application
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.test.{PlaySpecification, WithApplication}
import support.{Arbitraries, BookDbApi}

import scala.concurrent.Future

class AuthorsControllerSpec extends PlaySpecification with ScalaCheck with BookDbApi with Mockito with Arbitraries {
  "AuthorsController" should {
    "handle all /authors routes" in {
      implicit val arbitraryRequest = Arbitrary[PlayOperationVerifier](swaggerCheck.operationVerifier(_.startsWith("/v1/authors")))

      prop {
        requestVerifier: PlayOperationVerifier =>
          val Some(result) = route(requestVerifier.request)

          requestVerifier.responseVerifier.verify(result) must be equalTo ValidationResult.success
      }.setContext(new WithApplication(testApp) {})
    }
  }

  def testApp: Application = {
    val mockAuthorDAO = mock[AuthorDAO]

    mockAuthorDAO.all(any[Int], any[Int]) answers { _ => Future.successful(Arbitrary.arbitrary[Page[Author]].sample.getOrElse(Page.empty)) }
    mockAuthorDAO.findById(any[UUID]) answers { _ => Future.successful(Arbitrary.arbitrary[Option[Author]].sample.getOrElse(None)) }
    mockAuthorDAO.insert(any[Author]) returns Future.successful(Unit)
    mockAuthorDAO.delete(any[UUID]) returns Future.successful(Unit)

    new GuiceApplicationBuilder()
      .overrides(bind[AuthorDAO].toInstance(mockAuthorDAO))
      .build()
  }
}
