package models

import models.commons.{TaskFields, TaskType}
import models.persistences.Persistence
import play.api.libs.json._
import models.commons.CollectionsFields._
import helpers.Generator._
import play.api.data._
import play.api.data.Forms._
import models.commons.{ DataTypes => DT }

/**
  * Created by stephane on 01/12/2016.
  */

sealed trait Task extends Persistence {
  def getTime: Long
  override protected val dataType = Option(DT.Task)
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
        (obj \ DataType).as[String] match {
          case TaskType.GroupingTask => GroupingTask.groupingTasksReader.reads(obj)
          case TaskType.SimpleTask => SimpleTask.simpleTasksReader.reads(obj)
        }
      }
      case err => JsError(err.toString())
    }
  }

}

case class GroupingTask(
                         override val id: Option[String] = generateBSONId,
                         owner: User,
                         title: String,
                         description: String,
                         tasks: Set[Task],
                         override val dataType: Option[String] = Option(TaskType.GroupingTask)) extends Task {

  override def getTime: Long = tasks.foldLeft(0L){(acc, task) => task.getTime + acc}
}

case class SimpleTask(
                       override val id: Option[String] = generateBSONId,
                       owner: User,
                       title: String,
                       description: String,
                       time: Long,
                       override val dataType: Option[String] = Option(TaskType.SimpleTask)) extends Task {

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
        val taskType = (obj \ DataType).asOpt[String]
        JsSuccess(
          GroupingTask(
            id = id,
            owner = owner,
            title = title,
            description = description,
            dataType = taskType,
            tasks = tasks
          )
        )
      case _ => JsError("grouping task reader error")
    }
  }

  implicit val groupingTaskWriter: OWrites[GroupingTask] = new OWrites[GroupingTask] {
    override def writes(o: GroupingTask): JsObject = Json.obj(
      Id -> o.id,
      Owner -> o.owner,
      Title -> o.title,
      Description -> o.description,
      Tasks -> o.tasks.map( t => Task.taskWriter.writes(t)),
      DataType -> o.dataType
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
        JsSuccess(
          SimpleTask(
            id = id,
            owner = owner,
            title = title,
            description = description,
            time = time
          )
        )
      case _ => JsError("simple task reader error")
    }
  }

  implicit val simpleTaskWriter: OWrites[SimpleTask] = new OWrites[SimpleTask] {
    override def writes(o: SimpleTask): JsObject = Json.obj(
      Id -> o.id,
      Owner -> o.owner,
      Title -> o.title,
      Description -> o.description,
      Time -> o.time,
      DataType -> o.dataType
    )
  }

  val simpleTaskMapping = mapping(
    Id -> optional(text),
    Owner -> User.userMapping,
    Title -> nonEmptyText,
    Description -> nonEmptyText(6),
    Time -> longNumber,
    DataType -> optional(text)
  )(SimpleTask.apply)(SimpleTask.unapply)


}