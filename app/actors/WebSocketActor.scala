package actors

import akka.actor.Actor.Receive
import akka.actor.{ActorSystem, Actor, Props, ActorRef}
import models._
import models.persistences.Persistence
import play.api.libs.json.{Reads, Json, JsValue}
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.Future
import scala.util.Try

/**
  * Created by stephane on 06/12/2016.
  */
object WebSocketActor {
  def props(system: ActorRef, out: ActorRef, id: String) = Props(new WebSocketActor(system, out, id))
}

case class BroadCast(id: String, value: JsValue)

case class AddMe(id: String)

case class DelMe(id: String)

case class OActor(id: String, actorRef: ActorRef)

class ActorManager extends Actor {
  val actors = collection.mutable.HashMap[String, ActorRef]()

  override def receive = {
    case BroadCast(id, jsValue) => broadcast(id, jsValue)
    case AddMe(id) => actors.put(id, sender)
    case DelMe(id) => actors.remove(id)
  }

  private def broadcast(id: String, jsValue: JsValue) = {
    actors.filterNot(_._1 == id).foreach(f => f._2 ! BroadCast(id, jsValue))
  }

}


class WebSocketActor(val manager: ActorRef, val out: ActorRef, val id: String) extends Actor {


  private def parse(jsValue: JsValue) = {
    Try(jsValue.validate[Persistence]).map { p =>
      matcher(p.getOrElse(throw new Exception("Cannot parse jsValue on Persistence object")))
    }
  }

  private def matcher(persistence: Persistence) = persistence match {
    case st: SimpleTask => println("st")
    case gp: GroupingTask =>
      println(gp.id)
      println("gp")
    case tm: TaskManager =>
      println(tm.id)
      println("taskmanager")
    case um: UserManager => println("usermanager")
    case _ => println("others")
  }

  override def receive = {
    case jsValue: JsValue =>
      println(jsValue)
      parse(jsValue)
      out ! jsValue
    /*case task: Task =>
      task match {
        case st:SimpleTask =>
          println(st)
        case gt: GroupingTask => println(gt)
      }
    case _ => println("all")*/
    /*case jsValue: JsValue =>

      println(jsValue)
      val value = jsValue.asOpt[SimpleTask]
      Try {
        jsValue.as[GroupingTask]
      }.map(println)
      //val v = jsValue.asOpt[GroupingTask]
      value.map { st =>
        println(st)
      }
      //v.map{println}
      out ! jsValue
      //out ! ("Msg received " + msg)*/
  }
}