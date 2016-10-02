package roikonen.voterix.actors

import akka.actor.{Actor, ActorLogging}
import roikonen.voterix.actors.VotingSubject.{AcceptedVote, Vote}

object VotingSubject {
  final case class Vote(subject: String, value: String, voter: String)
  final case class AcceptedVote(subject: String, value: String, voter: String)
}

class VotingSubject extends Actor with ActorLogging {

  private var votes: Map[String, Integer] = Map()
  private var voters: Set[String] = Set()

  override def receive: Receive = {
    case vote: Vote => updateState(verify(vote))
  }

  def updateState(events: List[AcceptedVote]): Unit = events.foreach(updateState)

  def updateState(event: AcceptedVote): Unit = {
    log.info(s"Accepted vote: (Subject: ${event.subject} Value: ${event.value})")
    val currentNumOfVotes = votes.get(event.value)
    currentNumOfVotes match {
      case None => votes += (event.value -> 1)
      case Some(amountOfVotes) => votes += (event.value -> (amountOfVotes+1))
    }
    voters += event.voter
  }

  def verify(vote: Vote): List[AcceptedVote] = {
    if (voters.contains(vote.voter)) {
      log.info(s"Received vote from already voted voter: ${vote.voter}")
      List()
    } else {
      log.info(s"Received vote from a new voter: ${vote.voter}")
      List(AcceptedVote(self.path.name, vote.value, vote.voter))
    }
  }

}
