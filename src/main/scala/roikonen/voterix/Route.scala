package roikonen.voterix

import akka.http.scaladsl.server.Directives._

trait Route {

  val route =
    path("vote" / """\w+""".r / """\w+""".r) { (subject, value) =>
      extractClientIP { ip =>
        vote(subject, value, ip.value)
        complete(s"Thanks for voting $subject with value: $value!")
      }
    }

  def vote(subject: String, value: String, voter: String)
}
