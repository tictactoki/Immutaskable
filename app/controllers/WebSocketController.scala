package controllers

import actors.{WebSocketActor, JsonObject, ActorManager}
import akka.actor.{Props, ActorSystem}
import akka.stream.Materializer
import com.google.inject.Inject
import models.commons.CollectionsFields._
import models.persistences.Persistence
import play.api.Configuration
import play.api.libs.json.JsValue
import play.api.libs.streams.ActorFlow
import play.api.mvc.{Action, WebSocket, Controller}
import play.api.mvc.WebSocket.MessageFlowTransformer
import play.modules.reactivemongo.{ReactiveMongoComponents, MongoController, ReactiveMongoApi}

import scala.concurrent.ExecutionContext

/**
  * Created by stephane on 21/12/2016.
  */
class WebSocketController @Inject() (val reactiveMongoApi: ReactiveMongoApi)
                                    (implicit executionContext: ExecutionContext,
                                     configuration: Configuration,
                                     webJarAssets: WebJarAssets,
                                     system: ActorSystem,
                                     materializer: Materializer)
  extends Controller with MongoController with ReactiveMongoComponents  {

  lazy final val actorManager = system.actorOf(Props[ActorManager],"actor-manager")
  //implicit val messageFlowTransformer = MessageFlowTransformer.jsonMessageFlowTransformer[Persistence,Persistence]

  def socket = WebSocket.accept[JsValue,JsValue] { request =>
    //request.session.get(Id).map { id =>
      ActorFlow.actorRef(out => WebSocketActor.props(actorManager,out,"10"))
    //}.getOrElse(throw new Exception("connection error"))
  }

  def index = Action { implicit request =>
    Ok(views.html.websocket("test"))
  }

}
