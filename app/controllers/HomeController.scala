package controllers

import javax.inject._
import akka.actor.ActorSystem
import akka.stream.Materializer
import com.google.inject.Inject
import helpers.MongoDB
import models.User
import play.api._
import play.api.libs.json.{OWrites, Reads}
import play.api.mvc._
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.play.json.collection.JSONCollection
import models.commons.MongoCollectionNames._

import scala.concurrent.{Future, ExecutionContext}


/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(override val reactiveMongoApi: ReactiveMongoApi)
                              (implicit executionContext: ExecutionContext,
                               configuration: Configuration,
                               webJarAssets: WebJarAssets,
                               system: ActorSystem,
                               materializer: Materializer)
  extends CommonController(reactiveMongoApi) {

  override type P = User



  override implicit val mainCollection: Future[JSONCollection] = getJSONCollection(Users)
  override implicit val mainWriter: OWrites[User] = User.userWrites
  override implicit val mainReader: Reads[User] = User.userReader
  MongoDB.dbs.put(Users,mainCollection)

  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index = Action {
    //Ok(views.html.index("Your new application is ready."))
    Ok(views.html.login())
  }

}
