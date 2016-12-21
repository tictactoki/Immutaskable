package controllers

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

  override implicit val mainReader: Reads[Task] = Task.taskReader
  override implicit val mainWriter: OWrites[Task] = Task.taskWriter
  lazy val taskManagerCollection: Future[JSONCollection] = getJSONCollection(CN.TaskManagers)

  /*override protected def update(obj: Task)(implicit executionContext: ExecutionContext): Future[WriteResult] = {
    for {
      collection <- mainCollection
      test <- collection.update(fieldQuery(Id,obj._id.get),obj)
    } yield {
      test
    }
  }

  override protected def findById(id: String): Future[Option[Task]] = {
    for {
      collection <- mainCollection
      list <- collection.find(fieldQuery(Id,id)).cursor[Task]().collect[List]()
    } yield {
      list.headOption
    }
  }

  override protected def insert(obj: Task)(implicit executionContext: ExecutionContext): Future[WriteResult] = {
    mainCollection.flatMap(_.insert(obj))
  }

  override def getAll: Action[AnyContent] = Action.async { implicit request =>
    for {
      collection <- mainCollection
      list <- collection.find(Json.obj()).cursor[Task]().collect[List]()
    } yield {
      Ok(Json.toJson(list))
    }
  }*/
}
