package models

import java.util.Date
import helpers.Generator._
import models.persistences.Persistence
import play.api.libs.json.Json
import play.api.data._
import play.api.data.Forms._
import models.commons.CollectionsFields._
import models.commons.{DataTypes => DT}

/**
  * Created by stephane on 01/12/2016.
  */

case class SignIn(email: String, password: String)

object SignIn {
  implicit val signFormat = Json.format[SignIn]

  val signInForm = Form(
    mapping(
      Email -> email,
      Password -> nonEmptyText(6)
    )(SignIn.apply)(SignIn.unapply)
  )

}

case class User(
                 name: String,
                 firstName: String,
                 email: String,
                 password: String,
                 signDate: Date = new Date,
                 override val id: String = generateBSONId,
                 override protected val dataType: String = DT.User) extends Persistence

object User {

  implicit val userReader = Json.reads[User]
  implicit val userWrites = Json.writes[User]

  private def formApply(n: String, f:String, e: String, p: String): User = User(n, f, e, p, new Date, generateBSONId, DT.User)
  private def formUnapply(u: User) = Some(u.name, u.firstName, u.email, u.password)

  val userMapping = mapping(
    Name -> nonEmptyText,
    FirstName -> nonEmptyText,
    Email -> email,
    Password -> nonEmptyText(6)
  )(formApply)(formUnapply)



}