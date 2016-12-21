package models.persistences

import helpers.Generator._
import models.{UserManager, User, TaskManager, Task}
import play.api.libs.json._
import models.commons.CollectionsFields._
import models.commons.{ DataTypes => DT }

/**
  * Created by stephane on 20/12/2016.
  */
trait Persistence {
  lazy val _id: Option[String] = generateBSONId
}

object Persistence {

  /*implicit val persistenceWriter: OWrites[Persistence] = new OWrites[Persistence] {
    override def writes(o: Persistence): JsObject = o match {
      case task: Task => Task.taskWriter.writes(task)
      case taskManager: TaskManager => TaskManager.taskManagerWrites.writes(taskManager)
      case user: User => User.userWrites.writes(user)
      case userManager: UserManager => UserManager.userManagerWrites.writes(userManager)
      case _ => throw new Exception("Data persistence object doesn't exist")
    }
  }*/

  implicit val persistenceReader: Reads[Persistence] = new Reads[Persistence] {
    override def reads(json: JsValue): JsResult[Persistence] = json match {
      case obj: JsObject => {
        (obj \ DataType).as[String] match {
          case DT.Task => Task.taskReader.reads(json)
          case DT.TaskManager => TaskManager.taskManagerReader.reads(json)
          case DT.User => User.userReader.reads(json)
          case DT.UserManager => UserManager.userManagerReader.reads(json)
          case _ => throw new Exception("Data persistence object doesn't exist")
        }
      }
    }
  }

}