package models

import models.commons.{TaskFields, TaskType}
import play.api.libs.json._
import models.commons.CollectionsFields._
import helpers.Generator._
import play.api.data._
import play.api.data.Forms._

/**
  * Created by stephane on 01/12/2016.
  */

sealed trait Task {
  val taskType: String

  def getTime: Long

}

object Task {

  implicit val taskWriter = new OWrites[Task] {
    override def writes(o: Task): JsObject = o match {
      case gt:GroupingTask => GroupingTask.groupingTaskWriter.writes(gt)
      case st:SimpleTask => SimpleTask.simpleTaskFormat.writes(st)
      case _ => throw new Exception("Task error")
    }
  }

  implicit val taskReader = new Reads[Task] {
    override def reads(json: JsValue): JsResult[Task] = json match {
      case obj:JsObject => {
        (obj \ TaskFields.TaskType).as[String] match {
          case TaskType.GroupingTask => GroupingTask.groupingTasksReader.reads(obj)
          case TaskType.SimpleTask => SimpleTask.simpleTaskFormat.reads(obj)
        }
      }
      case err => JsError(err.toString())
    }
  }

}

case class GroupingTask(_id: Option[String] = generateBSONId, owner: User, title: String, description: String, tasks: Set[Task]) extends Task {
  override val taskType = TaskType.GroupingTask

  override def getTime: Long = tasks.foldLeft(0L){(acc, task) => task.getTime + acc}
}

case class SimpleTask(id: Option[String] = generateBSONId, owner: User, title: String, description: String, time: Long) extends Task {
  override val taskType = TaskType.SimpleTask

  override def getTime: Long = time
}


object GroupingTask {


  implicit val groupingTasksReader: Reads[GroupingTask] = new Reads[GroupingTask] {
    override def reads(json: JsValue): JsResult[GroupingTask] = json match {
      case obj: JsObject =>
        val id = (obj \ Id).asOpt[String]
        val owner = (obj \ Owner).as[User]
        val title = (obj \ Title).as[String]
        val description = (obj \ Description).as[String]
        val tasks = (obj \ Tasks).as[Set[Task]]
        JsSuccess(GroupingTask(id,owner,title,description,tasks))
      case _ => JsError("grouping task reader error")
    }
  }

  implicit val groupingTaskWriter: OWrites[GroupingTask] = new OWrites[GroupingTask] {
    override def writes(o: GroupingTask): JsObject = Json.obj(
      Id -> o._id,
      Owner -> o.owner,
      Title -> o.title,
      Description -> o.description,
      Tasks -> o.tasks.map( t => Task.taskWriter.writes(t))
    )
  }


}

object SimpleTask {

  implicit val simpleTaskFormat = Json.format[SimpleTask]

  val simpleTaskMapping = mapping(
    Id -> optional(text),
    Owner -> User.userMapping,
    Title -> nonEmptyText,
    Description -> nonEmptyText(6),
    Time -> longNumber
  )(SimpleTask.apply)(SimpleTask.unapply)


}