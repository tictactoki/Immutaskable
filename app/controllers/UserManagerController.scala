package controllers

import akka.actor.ActorSystem
import akka.stream.Materializer
import com.google.inject.Inject
import models.UserManager
import models.commons.MongoCollectionNames._
import play.api.Configuration
import play.api.libs.json.{OWrites, Reads}
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by stephane on 06/12/2016.
  */
class UserManagerController @Inject() (override val reactiveMongoApi: ReactiveMongoApi)
                                      (implicit executionContext: ExecutionContext,
                                       configuration: Configuration,
                                       webJarAssets: WebJarAssets,
                                       system: ActorSystem,
                                       materializer: Materializer)
  extends CommonController(reactiveMongoApi) {

  override type P = UserManager
  override implicit val mainCollection: Future[JSONCollection] = getJSONCollection(UserManagers)
  override implicit val mainReader: Reads[UserManager] = UserManager.userManagerReader
  override implicit val mainWriter: OWrites[UserManager] = UserManager.userManagerWrites

  //override protected def update(obj: P)(implicit executionContext: ExecutionContext): Future[WriteResult] = ???

  /*override protected def insert(obj: T)(implicit executionContext: ExecutionContext): Future[WriteResult] = {
    mainCollection.flatMap(_.insert(obj))
  }*/

  /*override protected def findById(id: String): Future[Option[P]] = {
    for {
      collection <- mainCollection
      list <- collection.find(fieldQuery(Id,id)).cursor[P]().collect[List]()
    } yield {
      list.headOption
    }
  }*/

 // override def getAll: Action[AnyContent] = ???
}
