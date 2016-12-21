package actors

import akka.actor.Actor.Receive
import akka.actor.{ActorSystem, Actor, Props, ActorRef}
import models._
import play.api.libs.json.{Reads, Json, JsValue}

import scala.util.Try

/**
  * Created by stephane on 06/12/2016.
  */
object WebSocketActor {
  def props(system: ActorRef, out: ActorRef, id: String) = Props(new WebSocketActor(system,out, id))
}

case class JsonObject(request: String, data: JsValue)

object JsonObject {
  implicit val jsonObjectFormat = Json.format[JsonObject]
}

case class BroadCast(id: String, value: JsValue)
case class Matcher(obj: JsonObject)
case class AddMe(id: String)
case class DelMe(id: String)

case class OActor(id: String, actorRef: ActorRef)

class ActorManager extends Actor {
  val actors = collection.mutable.HashMap[String,ActorRef]()

  override def receive = {
    case Matcher(obj) =>
    case BroadCast(id,jsValue) => broadcast(id,jsValue)
    case AddMe(id) => actors.put(id,sender)
    case DelMe(id) => actors.remove(id)
  }

  private def broadcast(id: String, jsValue: JsValue) = {
    actors.filterNot(_._1 == id).foreach(f => f._2 ! BroadCast(id,jsValue))
  }

  /*private def matcher(obj: JsonObject) = (obj.request,obj.data) match {
    case
  }*/

}


class WebSocketActor(val manager: ActorRef, val out: ActorRef, val id: String) extends Actor {


  def parser[T](implicit jsValue: JsValue, reads: Reads[T]) = Try{jsValue.as[T]}.toOption

  def parse(implicit jsValue: JsValue) = {
    val set = Set(parser[GroupingTask],parser[SimpleTask],parser[UserManager],parser[TaskManager])
    val it = set.filterNot(!_.isDefined).flatten
    it.headOption
  }

  def matcher(any: Product with Serializable) = any match {
    case st:SimpleTask => println("st")
    case gp:GroupingTask => println("gp")
    case tm:TaskManager => println("taskmanager")
    case um:UserManager => println("usermanager")
  }

  override def receive = {
    case jsValue: JsValue =>
      println(jsValue)
      parse(jsValue).map(matcher(_))
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