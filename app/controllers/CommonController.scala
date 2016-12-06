package controllers

import javax.inject.Inject

import actors.WebSocketActor
import akka.actor.ActorSystem
import akka.stream.Materializer
import models.Task
import play.api.libs.streams._
import controllers.crud.MongoCrud
import play.api.Configuration
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.WebSocket.MessageFlowTransformer
import play.api.mvc._
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}
import reactivemongo.play.json.collection.JSONCollection
import models.commons.CollectionsFields._
import scala.concurrent.{ExecutionContext, Future}
import play.modules.reactivemongo.json._

/**
  * Created by wong on 03/12/2016.
  */
class CommonController @Inject()(
                                  val reactiveMongoApi: ReactiveMongoApi,
                                  implicit val configuration: Configuration,
                                  implicit val webJarAssets: WebJarAssets,
                                  implicit val system: ActorSystem,
                                  implicit val materializer: Materializer
                                )(implicit executionContext: ExecutionContext)
  extends Controller with MongoController with ReactiveMongoComponents with MongoCrud {

  implicit val messageFlowTransformer = MessageFlowTransformer.jsonMessageFlowTransformer[Task,Task]

  def socket = WebSocket.accept[Task,Task] { request =>
    ActorFlow.actorRef(out => WebSocketActor.props(out))
  }

  def index = Action {
    Ok(views.html.websocket("webcocket"))
  }


}
