package controllers.crud


import controllers.CommonController
import models.commons.CollectionsFields._
import models.commons.{MongoCollectionNames => CN}
import models.persistences.Persistence
import play.api.libs.json.{JsObject, Json, OWrites, Reads}
import play.api.mvc.Action
import play.modules.reactivemongo.json._
import reactivemongo.api.commands.WriteResult
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.{ExecutionContext, Future}


trait MongoCrud { self: CommonController =>


  type P <: Persistence

  implicit val mainCollection: Future[JSONCollection]
  implicit val mainReader: Reads[P]
  implicit val mainWriter: OWrites[P]

  implicit protected val fieldQuery = (field: String, value: String) => Json.obj(field -> value)

  protected def getJSONCollection(name: String)(implicit executionContext: ExecutionContext) = reactiveMongoApi.database.map {_.collection[JSONCollection](name)}

  protected def findById(id: String)(implicit executionContext: ExecutionContext): Future[Option[P]] = {
    for {
      collection <- mainCollection
      list <- collection.find(fieldQuery(Id,id)).cursor[P]().collect[List]()
    } yield {
      list.headOption
    }
  }

  protected def delete(field: String, value: String)(implicit executionContext: ExecutionContext) = {
    mainCollection.flatMap(_.remove(Json.obj(field -> value)))
  }

  protected def insert(obj: P)(implicit executionContext: ExecutionContext): Future[WriteResult] = {
    mainCollection.flatMap(_.insert(obj))
  }

  protected def update(obj: P)(implicit executionContext: ExecutionContext): Future[WriteResult] = {
    mainCollection.flatMap(_.update(fieldQuery(Id,obj.id),obj))
  }

  def getAll(implicit executionContext: ExecutionContext) = Action.async { implicit request =>
    for {
      collection <- mainCollection
      list <- collection.find(Json.obj()).cursor[P]().collect[List]()
    } yield {
      Ok(Json.toJson(list))
    }
  }

  protected def findByField(field: String, value: String)(implicit executionContext: ExecutionContext,f: (String, String) => JsObject) = {
    for {
      collection <- mainCollection
      list <- collection.find(f(field,value)).cursor[P]().collect[List]()
    } yield {
      list.headOption
    }
  }


}
