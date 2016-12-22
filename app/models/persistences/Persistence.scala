package models.persistences

import helpers.Generator._
import models._
import play.api.libs.json._
import models.commons.CollectionsFields._
import models.commons.{ DataTypes => DT }

/**
  * Created by stephane on 20/12/2016.
  */
trait Persistence {
  val id: Option[String] = generateBSONId
  protected val dataType: Option[String]
}

object Persistence {

  implicit val persistenceReader: Reads[Persistence] = new Reads[Persistence] {
    override def reads(json: JsValue): JsResult[Persistence] = json match {
      case obj: JsObject => {
        (obj \ DataType).as[String] match {
          case DT.SimpleTask => SimpleTask.simpleTasksReader.reads(json)
          case DT.GroupingTask => GroupingTask.groupingTasksReader.reads(json)
          case DT.TaskManager => TaskManager.taskManagerReader.reads(json)
          case DT.User => User.userReader.reads(json)
          case DT.UserManager => UserManager.userManagerReader.reads(json)
          case _ => throw new Exception("Data persistence object doesn't exist")
        }
      }
      case other => throw new Exception("no json object matched")
    }
  }

}