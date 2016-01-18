package support

import java.io.File

import de.leanovate.swaggercheck.SwaggerChecks

trait BookDbApi {
  val swaggerCheck = SwaggerChecks(new File("../bookdb_api.yaml"))

}
