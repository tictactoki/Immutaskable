package controllers

import javax.inject.Singleton

import actors.{ActorManager, WebSocketActor}
import akka.actor.{ActorSystem, Props}
import akka.stream.Materializer
import com.google.inject.Inject
import play.api.Configuration
import play.api.libs.json.JsValue
import play.api.libs.streams.ActorFlow
import play.api.mvc.{Action, Controller, WebSocket}
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}

import scala.concurrent.ExecutionContext

/**
  * Created by stephane on 21/12/2016.
  */
@Singleton
class WebSocketController @Inject() (val reactiveMongoApi: ReactiveMongoApi)
                                    (implicit executionContext: ExecutionContext,
                                     configuration: Configuration,
                                     system: ActorSystem,
                                     materializer: Materializer)
  extends Controller with MongoController with ReactiveMongoComponents  {

  lazy final val actorManager = system.actorOf(Props[ActorManager],"actor-manager")

  def socket = WebSocket.accept[JsValue,JsValue] { request =>
    //request.session.get(Id).map { id =>
      ActorFlow.actorRef(out => WebSocketActor.props(actorManager,out,"10"))
    //}.getOrElse(throw new Exception("connection error"))
  }

  def index = Action { implicit request =>
    Ok(views.html.websocket("test"))
  }

}
