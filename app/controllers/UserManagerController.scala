package controllers

import akka.actor.ActorSystem
import akka.stream.Materializer
import com.google.inject.Inject
import controllers.crud.IMongoCrud
import models.UserManager
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
class UserManagerController @Inject() (
                                        override val reactiveMongoApi: ReactiveMongoApi,
                                        override implicit val configuration: Configuration,
                                        override implicit val webJarAssets: WebJarAssets,
                                        override implicit val system: ActorSystem,
                                        override implicit val materializer: Materializer)
                                      (implicit executionContext: ExecutionContext)
  extends CommonController(reactiveMongoApi,configuration,webJarAssets,system,materializer) with IMongoCrud {

  override type T = UserManager
  override implicit val mainCollection: Future[JSONCollection] = getJSONCollection(UserManagers)

  override protected def update(obj: T)(implicit executionContext: ExecutionContext): Future[WriteResult] = ???

  override protected def insert(obj: T)(implicit executionContext: ExecutionContext): Future[WriteResult] = {
    mainCollection.flatMap(_.insert(obj))
  }

  override protected def findById(id: String): Future[Option[T]] = {
    for {
      collection <- mainCollection
      list <- collection.find(fieldQuery(Id,id)).cursor[T]().collect[List]()
    } yield {
      list.headOption
    }
  }

  override def getAll: Action[AnyContent] = ???
}
