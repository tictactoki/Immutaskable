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

case class Sign()
case class User(name: String, firstName: String, email: String, protected val password: String, signDate: Date, override protected val dataType: Option[String] = Option(DT.User)) extends Persistence

object User {

  implicit val userReader = Json.reads[User]
  implicit val userWrites = Json.writes[User]

  val userMapping = mapping(
    Name -> nonEmptyText,
    FirstName -> nonEmptyText,
    Email -> email,
    Password -> nonEmptyText(6),
    SignDate -> date,
    DataType -> optional(text)
  ) (User.apply)(User.unapply)

}