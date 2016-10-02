package roikonen.voterix.actors

import akka.actor.{Actor, ActorRef, Props}

class VotingSubjectSupervisor extends Actor {

  private var votingSubjects: Map[String, ActorRef] = Map()

  override def receive: Receive = {
    case vote: VotingSubject.Vote => getVotingSubject(vote.subject).forward(vote)
  }

  def getVotingSubject(subject: String): ActorRef = {
    votingSubjects.getOrElse(subject, createVotingSubject(subject))
  }

  def createVotingSubject(subject: String): ActorRef = {
    val votingSubject = context.actorOf(Props[VotingSubject], subject)
    votingSubjects += (subject -> votingSubject)
    votingSubject
  }

}
