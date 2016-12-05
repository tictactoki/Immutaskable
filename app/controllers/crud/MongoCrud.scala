package controllers.crud


import controllers.CommonController
import models.{GroupingTask, Task, User}
import models.commons.CollectionsFields._
import play.api.libs.json.Json
import reactivemongo.api.commands.WriteResult
import reactivemongo.play.json.collection.JSONCollection
import models.commons.{MongoCollectionNames => CN}

import scala.concurrent.{ExecutionContext, Future}
import play.modules.reactivemongo.json._


trait IMongoCrud[T] {

  implicit val mainCollection: Future[JSONCollection]

  protected def findById(id: String): Future[Option[T]]

  protected def delete(field: String, value: String)(implicit executionContext: ExecutionContext) = {
    mainCollection.flatMap(_.remove(Json.obj(field -> value)))
  }

  protected def insert(obj: T)(implicit executionContext: ExecutionContext): Future[WriteResult]

  protected def update(obj: T)(implicit executionContext: ExecutionContext): Future[WriteResult]

}

trait MongoCrud { self: CommonController =>

  protected val fieldQuery = (field: String, value: String) => Json.obj(field -> value)

  protected def getJSONCollection(name: String)(implicit executionContext: ExecutionContext) = reactiveMongoApi.database.map {_.collection[JSONCollection](name)}

}
