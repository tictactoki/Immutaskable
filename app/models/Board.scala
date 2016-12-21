package models

import java.util.Date

import helpers.Generator._
import models.persistences.Persistence
import play.api.libs.json.Json


case class TaskManager(
                      task: Task,
                      pushDate: Date,
                      gapTime: Long
                    ) extends Persistence

object TaskManager {

  //implicit val taskManagerFormat = Json.format[TaskManager]
  implicit val taskManagerReader = Json.reads[TaskManager]
  implicit val taskManagerWrites = Json.writes[TaskManager]
}

case class UserManager(user: User, taskManagers: Set[TaskManager] = Set.empty) extends Persistence

object UserManager {
  //implicit val userManagerFormat = Json.format[UserManager]
  implicit val userManagerReader = Json.reads[UserManager]
  implicit val userManagerWrites = Json.writes[UserManager]
}