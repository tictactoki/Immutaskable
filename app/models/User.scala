package models

import java.util.Date
import helpers.Generator._
import play.api.libs.json.Json
import play.api.data._
import play.api.data.Forms._
import models.commons.CollectionsFields._

/**
  * Created by stephane on 01/12/2016.
  */

case class Sign()
case class User(_id: Option[String] = generateBSONId, name: String, firstName: String, protected val email: String, protected val password: String, signDate: Date)

object User {

  implicit val userFormat = Json.format[User]

  val userMapping = mapping(
    Id -> optional(nonEmptyText),
    Name -> nonEmptyText,
    FirstName -> nonEmptyText,
    Email -> email,
    Password -> nonEmptyText(6),
    SignDate -> date
  ) (User.apply)(User.unapply)

}