package helpers

import reactivemongo.bson.BSONObjectID

/**
  * Created by stephane on 01/12/2016.
  */
object Generator {

  def generateBSONId = Option(BSONObjectID.generate().stringify)

}
