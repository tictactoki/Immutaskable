package controllers

import javax.inject.Inject

import controllers.crud.MongoCrud
import play.api.Configuration
import play.api.libs.json.Json
import play.api.mvc.{AnyContent, Action, Result, Controller}
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}
import reactivemongo.play.json.collection.JSONCollection
import models.commons.CollectionsFields._
import scala.concurrent.{ExecutionContext, Future}
import play.modules.reactivemongo.json._

/**
  * Created by wong on 03/12/2016.
  */
abstract class CommonController @Inject()(
                                  val reactiveMongoApi: ReactiveMongoApi,
                                  implicit val configuration: Configuration,
                                  implicit val webJarAssets: WebJarAssets
                                )(implicit executionContext: ExecutionContext)
  extends Controller with MongoController with ReactiveMongoComponents with MongoCrud {


  def getAll: Action[AnyContent]


}
