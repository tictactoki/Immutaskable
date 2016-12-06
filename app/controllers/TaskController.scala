package controllers

import com.google.inject.Inject
import controllers.crud.IMongoCrud
import models.Task
import models.Task._
import play.api.Configuration
import play.api.libs.json.{JsError, JsSuccess, Json}
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
class TaskController @Inject() (
                                 override val reactiveMongoApi: ReactiveMongoApi,
                                 override implicit val configuration: Configuration,
                                 override implicit val webJarAssets: WebJarAssets)
                               (implicit executionContext: ExecutionContext)
extends CommonController(reactiveMongoApi,configuration,webJarAssets) with IMongoCrud
{

  type T = Task

  override implicit val mainCollection: Future[JSONCollection] = getJSONCollection(CN.Tasks)
  lazy val taskManagerCollection: Future[JSONCollection] = getJSONCollection(CN.TaskManagers)

  override protected def update(obj: Task)(implicit executionContext: ExecutionContext): Future[WriteResult] = ???

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
  }
}
