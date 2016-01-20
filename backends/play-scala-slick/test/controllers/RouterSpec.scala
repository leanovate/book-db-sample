package controllers

import de.leanovate.swaggercheck.playhelper._
import org.scalacheck.Arbitrary
import org.specs2.ScalaCheck
import play.api.Play
import play.api.mvc.EssentialAction
import play.api.routing.Router
import play.api.test.{FakeRequest, WithApplication, PlaySpecification}
import support.BookDbApi

class RouterSpec extends PlaySpecification with ScalaCheck with BookDbApi {
  "Router" should {
    "have all specified routes" in {
      implicit val arbitraryRequest = Arbitrary[FakeRequest[String]](swaggerCheck.requestGenerator())

      prop {
        request: FakeRequest[String] =>
          val router = Play.current.injector.instanceOf[Router]
          val handler = router.handlerFor(request)

          handler.isDefined aka s"Handler for ${request}" must beTrue
      }.setContext(new WithApplication() {})
    }
  }
}
