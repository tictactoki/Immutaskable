package models

import java.util.Date

import helpers.Generator._
import play.api.libs.json.Json

sealed trait CompositeBoard



case class TaskHandler(
                      _id: Option[String] = generateBSONId,
                      task: Task,
                      pushDate: Date,
                      gapTime: Long
                    )

object TaskHandler {

  implicit val taskHandlerFormat = Json.format[TaskHandler]

}
