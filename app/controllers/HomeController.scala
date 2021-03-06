package controllers

import javax.inject._
import akka.actor.ActorSystem
import akka.stream.Materializer
import com.google.inject.Inject
import helpers.MongoDB
import models.{SignIn, User}
import org.mindrot.jbcrypt.BCrypt
import play.api._
import play.api.data.Form
import play.api.libs.iteratee.Enumeratee
import play.api.libs.json.{Json, OWrites, Reads}
import play.api.mvc._
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.play.json.collection.JSONCollection
import models.commons.MongoCollectionNames._
import models.commons.CollectionsFields._
import scala.concurrent.{Future, ExecutionContext}
import models.User._

import scala.util.Try


/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class HomeController @Inject()(override val reactiveMongoApi: ReactiveMongoApi)
                              (implicit executionContext: ExecutionContext,
                               configuration: Configuration,
                               system: ActorSystem,
                               materializer: Materializer)
  extends CommonController(reactiveMongoApi) {


  override type P = User

  override implicit val mainCollection: Future[JSONCollection] = getJSONCollection(Users)
  override implicit val mainWriter: OWrites[User] = User.userWrites
  override implicit val mainReader: Reads[User] = User.userReader
  MongoDB.dbs.put(Users, mainCollection)

  private final lazy val userForm = Form(User.userMapping)
  private final lazy val salt = BCrypt.gensalt()

  protected def checkEmail(email: String) = {
    findByField(Email, email).map(_.isDefined)
  }

  protected def checkSignIn(signIn: SignIn) = {
    findByField(Email, signIn.email).map { ou =>
      ou.map { user =>
        (BCrypt.checkpw(signIn.password, user.password),user)
          //Redirect(routes.HomeController.dashBoard).withSession(Session(createUserDataSession(user)))
        //}
        //else BadRequest(views.html.login(Option(userForm), Option(SignIn.signInForm),true))
      }.getOrElse(false,null)
    }
  }

  /**
    * Create an Action to render an HTML page with a welcome message.
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/`.
    */
  def index = Action {
    Ok(views.html.test())
  }


  protected def createUserDataSession(implicit user: User) = {
    Map(Id -> user.id, Email -> user.email)

  }

  def logout = Action {
    Ok(views.html.test()).withNewSession
  }

  def dashBoard = Action  { implicit request =>
    request.session.get(Id).map { id =>
      println(id)
      Ok(views.html.dashboard())
    }.getOrElse(Ok(views.html.test()))
  }


  def fake = Action { implicit request =>
    Ok("")
  }

  def login = Action.async { implicit request =>
    SignIn.signInForm.bindFromRequest().fold(
      hasErrors => {
        getJsonFormError(hasErrors)
      },
      data => {
        checkSignIn(data).map { case (valid, user) =>
          if (valid) {
            // if valid user doesn't be null
            Redirect(routes.HomeController.dashBoard).withSession(Session(createUserDataSession(user)))
          }
          else {
            Unauthorized(Json.toJson(data.email))
          }
        }
      }

    )
  }

  def signUp = Action.async { implicit request =>
    userForm.bindFromRequest().fold(
      hasErrors => getJsonFormError(hasErrors),
      u => {
        checkEmail(u.email).map { exist =>
          if (!exist) {
            val cryptPassword = BCrypt.hashpw(u.password, salt)
            implicit val newUser = User(u.name, u.firstName, u.email, cryptPassword)
            insert(newUser)
            println(newUser)
            Redirect(routes.HomeController.dashBoard).withSession(Session(createUserDataSession))
          }
          else Conflict("Email already exist")
        }
      }
    )
  }


}
