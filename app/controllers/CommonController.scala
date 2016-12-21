package controllers

import javax.inject.Inject

import actors.{JsonObject, ActorManager, WebSocketActor}
import akka.actor.{Props, ActorSystem}
import akka.stream.Materializer
import models.{User, Task}
import play.api.libs.iteratee.Concurrent
import play.api.libs.streams._
import controllers.crud.MongoCrud
import play.api.Configuration
import play.api.libs.json.{OWrites, Reads, JsValue, Json}
import play.api.mvc.WebSocket.MessageFlowTransformer
import play.api.mvc._
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}
import reactivemongo.play.json.collection.JSONCollection
import models.commons.CollectionsFields._
import scala.concurrent.{ExecutionContext, Future}
import models.commons.{ MongoCollectionNames => CN }
import play.modules.reactivemongo.json._

/**
  * Created by wong on 03/12/2016.
  */
abstract class CommonController @Inject()(val reactiveMongoApi: ReactiveMongoApi)
                                (implicit executionContext: ExecutionContext,
                                 configuration: Configuration,
                                 webJarAssets: WebJarAssets,
                                 system: ActorSystem,
                                 materializer: Materializer)
  extends Controller with MongoController with ReactiveMongoComponents with MongoCrud
