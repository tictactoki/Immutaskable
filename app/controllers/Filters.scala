package controllers

import javax.inject.Inject

import akka.stream.Materializer
import play.api.Environment
import play.api.http.DefaultHttpFilters
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by stephane on 16/01/2017.
  */

class TLSFilters @Inject() (
                             implicit val mat: Materializer,
                             ec: ExecutionContext, env: Environment) extends Filter {
  def apply(nextFilter: RequestHeader => Future[Result])(requestHeader: RequestHeader): Future[Result] = {
    if(requestHeader.headers.get("x-forwarded-proto").getOrElse("http") != "https" && env.mode == play.api.Mode.Prod)
      Future.successful(Results.MovedPermanently("https://" + requestHeader.host + requestHeader.uri))
    else
      nextFilter(requestHeader).map { _.withHeaders("Strict-Transport-Security" -> "max-age=31536000") }
  }
}

class Filters @Inject() (tls: TLSFilters) extends DefaultHttpFilters(tls)
