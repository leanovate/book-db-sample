# Sample implementations of a book database

This is supposed to provide multiple implementations of a book database using various frameworks.

This is a place for experiments and discussion. Nothing in here has any use in the real world, but might provide a starting point for a real project.

## Rules of conduct

* [/backends](/backends) contains all backend implementations dealing with persistence. Each implementation is supposed to ...
  * ... implement a REST API as specified in this swagger [bookdb_api.yaml](/backends/bookdb_api.yaml)
  * ... provide a `run.sh` shell script building the sources (if necessary) and starting the service on port `8080`. If a specific database is required it should be started as well using docker.
* [/blackbox-tests](/blackbox-tests) contains all black-box tests of the backend service. Each implementation is support to ...
  * ... only use the REST API on port `8080`
  * ... create its own fixtures 
  * ... provide a `run.sh` building the sources (if necessary) and executing the tests.
