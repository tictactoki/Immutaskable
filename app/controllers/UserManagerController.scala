package controllers

import javax.inject.Singleton

import akka.actor.ActorSystem
import akka.stream.Materializer
import com.google.inject.Inject
import helpers.MongoDB
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
@Singleton
class UserManagerController @Inject() (override val reactiveMongoApi: ReactiveMongoApi)
                                      (implicit executionContext: ExecutionContext,
                                       configuration: Configuration,
                                       system: ActorSystem,
                                       materializer: Materializer)
  extends CommonController(reactiveMongoApi) {

  override type P = UserManager
  override implicit val mainCollection: Future[JSONCollection] = getJSONCollection(UserManagers)
  MongoDB.dbs.put(UserManagers,mainCollection)
  override implicit val mainReader: Reads[UserManager] = UserManager.userManagerReader
  override implicit val mainWriter: OWrites[UserManager] = UserManager.userManagerWrites

}
