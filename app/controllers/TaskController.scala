package controllers

import actors.MongoDB
import akka.actor.ActorSystem
import akka.stream.Materializer
import com.google.inject.Inject
import models.Task
import play.api.Configuration
import play.api.libs.json._
import play.api.mvc.{AnyContent, Result, Action, Controller}
import play.modules.reactivemongo.{ReactiveMongoComponents, MongoController, ReactiveMongoApi}
import reactivemongo.api.commands.WriteResult
import reactivemongo.play.json.collection.JSONCollection
import play.modules.reactivemongo.json._
import utils.Errors
import models.commons.CollectionsFields._
import models.commons.{MongoCollectionNames => CN}
import scala.concurrent.{Future, ExecutionContext}

/**
  * Created by stephane on 02/12/2016.
  */
class TaskController @Inject() (override val reactiveMongoApi: ReactiveMongoApi)
                               (implicit executionContext: ExecutionContext,
                                configuration: Configuration,
                                webJarAssets: WebJarAssets,
                                system: ActorSystem,
                                materializer: Materializer)
extends CommonController(reactiveMongoApi)
{


  override type P = Task

  override implicit val mainCollection: Future[JSONCollection] = getJSONCollection(CN.Tasks)
  MongoDB.dbs.put(CN.Tasks,mainCollection)

  override implicit val mainReader: Reads[Task] = Task.taskReader
  override implicit val mainWriter: OWrites[Task] = Task.taskWriter
  lazy val taskManagerCollection: Future[JSONCollection] = getJSONCollection(CN.TaskManagers)



}
