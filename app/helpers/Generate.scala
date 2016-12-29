package helpers

import reactivemongo.bson.BSONObjectID

/**
  * Created by stephane on 01/12/2016.
  */
object Generator {

  def generateBSONId = BSONObjectID.generate().stringify

}
