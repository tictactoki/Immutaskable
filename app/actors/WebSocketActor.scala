package actors

import akka.actor.Actor.Receive
import akka.actor.{Props, Actor, ActorRef}
import models.{GroupingTask, Task, SimpleTask}
import play.api.libs.json.{Json, JsValue}

import scala.util.Try

/**
  * Created by stephane on 06/12/2016.
  */
object WebSocketActor {
  def props(out: ActorRef) = Props(new WebSocketActor(out))
}




class WebSocketActor(out: ActorRef) extends Actor {
  override def receive = {
    case task: Task =>
      task match {
        case st:SimpleTask => println(st)
        case gt: GroupingTask => println(gt)
      }
    case _ => println("all")
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