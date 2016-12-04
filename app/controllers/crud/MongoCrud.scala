package controllers.crud


import controllers.CommonController
import models.{GroupingTask, Task, User}
import models.commons.CollectionsFields._
import play.api.libs.json.Json
import reactivemongo.play.json.collection.JSONCollection
import models.commons.{MongoCollectionNames => CN}

import scala.concurrent.{ExecutionContext, Future}
import play.modules.reactivemongo.json._
/**
  * Created by wong on 03/12/2016.
  */
trait MongoCrud {

  type Type

  private def getCollectionName = {
    this match {
      case t:Task => CN.Tasks
      case g:GroupingTask => CN.Tasks
      case u:User => CN.Users
      case _ => throw new Exception("errors")
    }
  }

  protected val idQuery = (id: String) => Json.obj(Id -> id)

  implicit lazy val mainCollection: Future[JSONCollection] = getJSONCollection(getCollectionName)

  protected def getJSONCollection(name: String): Future[JSONCollection]

  protected def findById[T](id: String)(implicit exec: ExecutionContext) = {
    for {
      collection <- mainCollection
      list <- collection.find(idQuery(id)).cursor[T]().collect[List]()
    } yield {
      list.headOption
    }
  }

  protected def findByField[T](field: String, value: String)(implicit collection: Future[JSONCollection]) = {
    val query = Json.obj(field -> value)
    for{
      collection <- mainCollection
      list <- collection.find(query).cursor[T]().collect[List]()
    } yield {
      list
    }
  }

}
