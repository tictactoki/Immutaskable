package models

import java.util.Date

import helpers.Generator._
import models.persistences.Persistence
import play.api.libs.json.Json
import models.commons.{DataTypes => DT}

case class TaskManager(
                        task: Task,
                        pushDate: Date,
                        gapTime: Long,
                        override val id: String = generateBSONId,
                        override protected val dataType: String = DT.TaskManager
                      ) extends Persistence

object TaskManager {

  implicit val taskManagerReader = Json.reads[TaskManager]
  implicit val taskManagerWrites = Json.writes[TaskManager]
}

case class UserManager(
                        user: User,
                        taskManagers: Set[TaskManager] = Set.empty,
                        override val id: String = generateBSONId,
                        override protected val dataType: String = DT.UserManager
                      ) extends Persistence

object UserManager {
  implicit val userManagerReader = Json.reads[UserManager]
  implicit val userManagerWrites = Json.writes[UserManager]
}