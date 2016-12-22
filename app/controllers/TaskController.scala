package controllers

import akka.actor.ActorSystem
import akka.stream.Materializer
import com.google.inject.Inject
import helpers.MongoDB
import models.Task
import models.commons.{MongoCollectionNames => CN}
import play.api.Configuration
import play.api.libs.json._
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by stephane on 02/12/2016.
  */
class TaskController @Inject()(override val reactiveMongoApi: ReactiveMongoApi)
                              (implicit executionContext: ExecutionContext,
                               configuration: Configuration,
                               webJarAssets: WebJarAssets,
                               system: ActorSystem,
                               materializer: Materializer)
  extends CommonController(reactiveMongoApi) {


  override type P = Task

  override implicit val mainCollection: Future[JSONCollection] = getJSONCollection(CN.Tasks)
  MongoDB.dbs.put(CN.Tasks, mainCollection)

  override implicit val mainReader: Reads[Task] = Task.taskReader
  override implicit val mainWriter: OWrites[Task] = Task.taskWriter
  lazy val taskManagerCollection: Future[JSONCollection] = getJSONCollection(CN.TaskManagers)


}
