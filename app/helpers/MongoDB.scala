package helpers

import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.Future

/**
  * Created by stephane on 22/12/2016.
  */

object MongoDB {

  lazy val dbs = collection.mutable.HashMap[String, Future[JSONCollection]]()

}
