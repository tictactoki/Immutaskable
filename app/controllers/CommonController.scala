package controllers

import javax.inject.{Singleton, Inject}

import akka.actor.ActorSystem
import akka.stream.Materializer
import controllers.crud.MongoCrud
import models.commons.{MongoCollectionNames => CN}
import play.api.Configuration
import play.api.mvc._
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}

import scala.concurrent.ExecutionContext

/**
  * Created by wong on 03/12/2016.
  */
@Singleton
abstract class CommonController @Inject()(val reactiveMongoApi: ReactiveMongoApi)
                                (implicit executionContext: ExecutionContext,
                                 configuration: Configuration,
                                 system: ActorSystem,
                                 materializer: Materializer)
  extends Controller with MongoController with ReactiveMongoComponents with MongoCrud {

  protected val errorLoginAccess = Unauthorized("You can't access without log in")


}
