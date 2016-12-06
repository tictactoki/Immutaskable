package models

import java.util.Date

import helpers.Generator._
import play.api.libs.json.Json


case class TaskManager(
                      _id: Option[String] = generateBSONId,
                      task: Task,
                      pushDate: Date,
                      gapTime: Long
                    )

object TaskManager {
  implicit val taskManagerFormat = Json.format[TaskManager]
}

case class UserManager(user: User, taskManagers: Set[TaskManager] = Set.empty)

object UserManager {
  implicit val userManagerFormat = Json.format[UserManager]
}