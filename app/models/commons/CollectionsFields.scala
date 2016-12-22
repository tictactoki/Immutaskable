package models.commons

/**
  * Created by stephane on 01/12/2016.
  */
object CollectionsFields {

  final val Id = "_id"
  final val Name = "name"
  final val FirstName = "firstName"
  final val Password = "password"
  final val Email = "email"
  final val Description = "description"
  final val SignDate = "sign_date"
  final val Owner = "owner"
  final val Title = "title"
  final val Time = "time"
  final val Tasks = "tasks"
  final val DataType = "data_type"


}

object DataTypes {
  final val SimpleTask = "simple_task"
  final val GroupingTask = "grouping_task"
  final val Task = "task"
  final val User = "user"
  final val TaskManager = "task_manager"
  final val UserManager = "user_manager"
}