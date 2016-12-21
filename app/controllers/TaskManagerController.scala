package controllers

import akka.actor.ActorSystem
import akka.stream.Materializer
import com.google.inject.Inject
import models.{TaskManager, Task}
import play.api.Configuration
import play.api.libs.json.{Reads, OWrites}
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
class TaskManagerController @Inject() (override val reactiveMongoApi: ReactiveMongoApi)
                               (implicit executionContext: ExecutionContext,
                                configuration: Configuration,
                                webJarAssets: WebJarAssets,
                                system: ActorSystem,
                                materializer: Materializer)
  extends CommonController(reactiveMongoApi)  {

  override type P = TaskManager
  override implicit val mainCollection: Future[JSONCollection] = getJSONCollection(TaskManagers)
  override implicit val mainReader: Reads[TaskManager] = TaskManager.taskManagerReader
  override implicit val mainWriter: OWrites[TaskManager] = TaskManager.taskManagerWrites

 // override protected def update(obj: TaskManager)(implicit executionContext: ExecutionContext): Future[WriteResult] = ???

  /*override protected def insert(obj: TaskManager)(implicit executionContext: ExecutionContext): Future[WriteResult] = {
    mainCollection.flatMap(_.insert(obj))
  }*/

  /*override protected def findById(id: String): Future[Option[TaskManager]] = {
    for {
      collection <- mainCollection
      list <- collection.find(fieldQuery(Id,id)).cursor[TaskManager]().collect[List]()
    } yield {
      list.headOption
    }
  }

  override def getAll: Action[AnyContent] = ???*/

}

