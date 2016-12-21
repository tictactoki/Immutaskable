package models

import models.commons.{TaskFields, TaskType}
import models.persistences.Persistence
import play.api.libs.json._
import models.commons.CollectionsFields._
import helpers.Generator._
import play.api.data._
import play.api.data.Forms._

/**
  * Created by stephane on 01/12/2016.
  */

sealed trait Task extends Persistence {
  val taskType: Option[String]
  def getTime: Long

}

object Task {

  implicit val taskWriter: OWrites[Task] = new OWrites[Task] {
    override def writes(o: Task): JsObject = o match {
      case gt:GroupingTask => GroupingTask.groupingTaskWriter.writes(gt)
      case st:SimpleTask => SimpleTask.simpleTaskWriter.writes(st)
      case _ => throw new Exception("Task error")
    }
  }

  implicit val taskReader: Reads[Task] = new Reads[Task] {
    override def reads(json: JsValue): JsResult[Task] = json match {
      case obj:JsObject => {
        (obj \ TaskFields.TaskType).as[String] match {
          case TaskType.GroupingTask => GroupingTask.groupingTasksReader.reads(obj)
          case TaskType.SimpleTask => SimpleTask.simpleTasksReader.reads(obj)
        }
      }
      case err => JsError(err.toString())
    }
  }

}

case class GroupingTask(
                         owner: User,
                         title: String,
                         description: String,
                         tasks: Set[Task],
                         override val taskType: Option[String] = Option(TaskType.GroupingTask)) extends Task {

  override def getTime: Long = tasks.foldLeft(0L){(acc, task) => task.getTime + acc}
}

case class SimpleTask(
                       owner: User,
                       title: String,
                       description: String,
                       time: Long,
                       override val taskType: Option[String] = Option(TaskType.SimpleTask)) extends Task {

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
        val taskType = (obj \ TaskFields.TaskType).asOpt[String]
        JsSuccess(GroupingTask(owner = owner,title = title, description = description,taskType = taskType, tasks = tasks))
      case _ => JsError("grouping task reader error")
    }
  }

  implicit val groupingTaskWriter: OWrites[GroupingTask] = new OWrites[GroupingTask] {
    override def writes(o: GroupingTask): JsObject = Json.obj(
      Id -> o._id,
      Owner -> o.owner,
      Title -> o.title,
      Description -> o.description,
      Tasks -> o.tasks.map( t => Task.taskWriter.writes(t)),
      TaskFields.TaskType -> o.taskType
    )
  }


}

object SimpleTask {

  //implicit val simpleTaskFormat = Json.format[SimpleTask]

  implicit val simpleTasksReader: Reads[SimpleTask] = new Reads[SimpleTask] {
    override def reads(json: JsValue): JsResult[SimpleTask] = json match {
      case obj: JsObject =>
        val id = (obj \ Id).asOpt[String]
        val owner = (obj \ Owner).as[User]
        val title = (obj \ Title).as[String]
        val description = (obj \ Description).as[String]
        val time = (obj \ Time).as[Long]
        val taskType = (obj \ TaskFields.TaskType).asOpt[String]
        JsSuccess(SimpleTask(owner = owner, title = title, taskType = taskType,description = description,time = time))
      case _ => JsError("simple task reader error")
    }
  }

  implicit val simpleTaskWriter: OWrites[SimpleTask] = new OWrites[SimpleTask] {
    override def writes(o: SimpleTask): JsObject = Json.obj(
      Id -> o._id,
      Owner -> o.owner,
      Title -> o.title,
      Description -> o.description,
      Time -> o.time,
      TaskFields.TaskType -> o.taskType
    )
  }

  val simpleTaskMapping = mapping(
    Owner -> User.userMapping,
    Title -> nonEmptyText,
    Description -> nonEmptyText(6),
    Time -> longNumber,
    TaskFields.TaskType -> optional(text)
  )(SimpleTask.apply)(SimpleTask.unapply)


}