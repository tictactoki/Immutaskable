package controllers

import com.google.inject.Inject
import controllers.crud.IMongoCrud
import models.{TaskManager, Task}
import play.api.Configuration
import play.api.mvc.{AnyContent, Action}
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.commands.WriteResult
import reactivemongo.play.json.collection.JSONCollection
import models.commons.MongoCollectionNames._
import scala.concurrent.{Future, ExecutionContext}
import models.commons.CollectionsFields._
import play.modules.reactivemongo.json._
/**
  * Created by stephane on 06/12/2016.
  */
class TaskManagerController @Inject() (
                                 override val reactiveMongoApi: ReactiveMongoApi,
                                 override implicit val configuration: Configuration,
                                 override implicit val webJarAssets: WebJarAssets)
                               (implicit executionContext: ExecutionContext)
  extends CommonController(reactiveMongoApi,configuration,webJarAssets) with IMongoCrud {

  override type T = TaskManager
  override implicit val mainCollection: Future[JSONCollection] = getJSONCollection(TaskManagers)


  override protected def update(obj: TaskManager)(implicit executionContext: ExecutionContext): Future[WriteResult] = ???

  override protected def insert(obj: TaskManager)(implicit executionContext: ExecutionContext): Future[WriteResult] = {
    mainCollection.flatMap(_.insert(obj))
  }

  override protected def findById(id: String): Future[Option[TaskManager]] = {
    for {
      collection <- mainCollection
      list <- collection.find(fieldQuery(Id,id)).cursor[TaskManager]().collect[List]()
    } yield {
      list.headOption
    }
  }

  override def getAll: Action[AnyContent] = ???

}

