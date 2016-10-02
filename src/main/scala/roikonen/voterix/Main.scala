package roikonen.voterix

import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import roikonen.voterix.actors.{VotingSubject, VotingSubjectSupervisor}

object Main extends App with Route {

  implicit val system = ActorSystem("voterix-system")
  implicit val materializer = ActorMaterializer()

  val (hostname, port) = ("localhost", 8080)

  val votingSubjectSupervisor = system.actorOf(Props[VotingSubjectSupervisor], "VotingSubjectSupervisor")

  Http().bindAndHandle(route, hostname, port)

  override def vote(subject: String, value: String, voter: String): Unit = {
    votingSubjectSupervisor ! VotingSubject.Vote(subject, value, voter)
  }
}