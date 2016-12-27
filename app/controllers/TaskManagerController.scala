package controllers

import javax.inject.Singleton

import akka.actor.ActorSystem
import akka.stream.Materializer
import com.google.inject.Inject
import helpers.MongoDB
import models.TaskManager
import models.commons.MongoCollectionNames._
import play.api.Configuration
import play.api.libs.json.{OWrites, Reads}
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by stephane on 06/12/2016.
  */
@Singleton
class TaskManagerController @Inject()(override val reactiveMongoApi: ReactiveMongoApi)
                                     (implicit executionContext: ExecutionContext,
                                      configuration: Configuration,
                                      webJarAssets: WebJarAssets,
                                      system: ActorSystem,
                                      materializer: Materializer)
  extends CommonController(reactiveMongoApi) {

  override type P = TaskManager
  override implicit val mainCollection: Future[JSONCollection] = getJSONCollection(TaskManagers)
  MongoDB.dbs.put(TaskManagers, mainCollection)
  override implicit val mainReader: Reads[TaskManager] = TaskManager.taskManagerReader
  override implicit val mainWriter: OWrites[TaskManager] = TaskManager.taskManagerWrites

}

