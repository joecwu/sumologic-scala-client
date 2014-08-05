package tw.joecwu.sumologic.scala.client

import dispatch._
import scala.collection.JavaConversions._
import scala.concurrent.{TimeoutException, Future, ExecutionContext}
import java.util.concurrent.Executors
import java.util.{Date,Locale}
import java.net.URLEncoder
import org.json4s._
import org.json4s.native.JsonMethods._
import org.json4s

case class ArticleLog(aid : String, geo_country : String, geo_region : String, geo_city : String, n_plmn : String, o_plmn : String, time : Long)
case class CategoryLog(cids : List[String], geo_country : String, geo_region : String, geo_city : String, n_plmn : String, o_plmn : String, time : Long)
case class LocationInfo(geo_country : String, geo_region : String, geo_city : String, lastTime : Long, count: Int)
case class OperatorInfo(o_plmn : String, n_plmn : String, lastTime : Long, count: Int)

object SumologicHelper extends Logger {
  	implicit val ec = ExecutionContext.fromExecutorService(Executors.newCachedThreadPool())
    implicit val formats = org.json4s.DefaultFormats

	val HTTP_AUTH = "PUT_YOUR_BASIC_AUTHENTICATION_STRING Base64encoded(ID:PW)"

	val SUMO_HOST = "https://client.sumologic.com"

	val SUMO_SEARCH_API = SUMO_HOST + "/client/v1/logs/search"

	val TOKENIZER = "%3d"

	val DATE_FORMATE = "yyyy-MM-dd'T'HH:mm:ss"

	val SEARCH_INNER_TIMEOUT = 60000

	val http = new dispatch.Http {
		import com.ning.http.client._
		val builder = new AsyncHttpClientConfig.Builder()
		builder.setAllowPoolingConnection(true)
		  .setAllowSslConnectionPool(true)
		  .setCompressionEnabled(true)
		  .setRequestTimeoutInMs(SEARCH_INNER_TIMEOUT)
		  .setConnectionTimeoutInMs(SEARCH_INNER_TIMEOUT)
		override val client = new AsyncHttpClient(builder.build())
	}

    val sdf = new java.text.SimpleDateFormat(DATE_FORMATE, Locale.ENGLISH);

	def searchArticles(dsn : String, from : Date, to : Date) : Future[List[ArticleLog]] = {
		log.info(s"searchArticles DeviceSN:[${dsn}] from:[${from.toString}] to:[${to.toString}]")

		val query_string = URLEncoder.encode(s"""_view=prism_dashboard_articles_view "${TOKENIZER}${dsn}" ""","UTF-8")

		val urlString = s"""${SUMO_SEARCH_API}?q=${query_string}&from=${sdf.format(from)}&to=${sdf.format(to)}"""

		log.debug("searchArticles url:"+urlString)

	    val svc = url(urlString).addHeader("Accept-Encoding","gzip").addHeader("Authorization",HTTP_AUTH)

		http( svc OK GzipJson ).either.map{ resp =>

			resp match {
				case Right(json) => {
			    	json match {
				      case JArray(items) => items.map(parseArticle)
				      case _ => List[ArticleLog]()
				    }
				}
				case Left(err) => {
					log.error("searchArticles got error: " + err)
					List[ArticleLog]()
				}
        	}
    	}

	}

	def searchCategories(dsn : String, from : Date, to : Date) : Future[List[CategoryLog]] = {
		log.info(s"searchCategories DeviceSN:[${dsn}] from:[${from.toString}] to:[${to.toString}]")

		val query_string = URLEncoder.encode(s"""_view=prism_dashboard_categories_view "${TOKENIZER}${dsn}" ""","UTF-8")

		val urlString = s"""${SUMO_SEARCH_API}?q=${query_string}&from=${sdf.format(from)}&to=${sdf.format(to)}"""

		log.debug("searchCategories url:"+urlString)

	    val svc = url(urlString).addHeader("Accept-Encoding","gzip").addHeader("Authorization",HTTP_AUTH)

	    http( svc OK GzipJson ).either.map{ resp =>

			resp match {
				case Right(json) => {
			    	json match {
				      case JArray(items) => items.map(parseCategory)
				      case _ => List[CategoryLog]()
				    }
				}
				case Left(err) => {
					log.error("searchCategories got error: " + err)
					List[CategoryLog]()
				}
        	}
    	}
	}

	def searchLocations(dsn : String, from : Date, to : Date) : Future[List[LocationInfo]] = {
		log.info(s"searchLocations DeviceSN:[${dsn}] from:[${from.toString}] to:[${to.toString}]")

		val query_string = URLEncoder.encode(s"""_view=prism_geo_base_view "${TOKENIZER}${dsn}" | max(_messagetime),count group by geo_city, geo_country, geo_region""","UTF-8")

		val urlString = s"""${SUMO_SEARCH_API}?q=${query_string}&from=${sdf.format(from)}&to=${sdf.format(to)}"""

		log.debug("searchLocations url:"+urlString)

	    val svc = url(urlString).addHeader("Accept-Encoding","gzip").addHeader("Authorization",HTTP_AUTH)

	    http( svc OK GzipJson ).either.map{ resp =>

			resp match {
				case Right(json) => {
			    	json match {
				      case JArray(items) => items.map(parseLocation)
				      case _ => List[LocationInfo]()
				    }
				}
				case Left(err) => {
					log.error("searchLocations got error: " + err)
					List[LocationInfo]()
				}
        	}
    	}
	}

	def searchOperators(dsn : String, from : Date, to : Date) : Future[List[OperatorInfo]] = {
		log.info(s"searchOperators DeviceSN:[${dsn}] from:[${from.toString}] to:[${to.toString}]")

		val query_string = URLEncoder.encode(s"""_view=prism_geo_base_view "${TOKENIZER}${dsn}" | max(_messagetime),count group by x_htc_network_plmn, x_htc_operator_plmn""","UTF-8")

		val urlString = s"""${SUMO_SEARCH_API}?q=${query_string}&from=${sdf.format(from)}&to=${sdf.format(to)}"""

		log.debug("searchOperators url:"+urlString)

	    val svc = url(urlString).addHeader("Accept-Encoding","gzip").addHeader("Authorization",HTTP_AUTH)

	    http( svc OK GzipJson ).either.map{ resp =>

			resp match {
				case Right(json) => {
			    	json match {
				      case JArray(items) => items.map(parseOperator)
				      case _ => List[OperatorInfo]()
				    }
				}
				case Left(err) => {
					log.error("searchOperators got error: " + err)
					List[OperatorInfo]()
				}
        	}
    	}
	}

	def parseArticle(item : json4s.JValue) : ArticleLog = {
		ArticleLog(
			aid = (item \ "aid").extract[String],
			geo_country = (item \ "geo_country").extract[String],
			geo_region = (item \ "geo_region").extract[String],
			geo_city = (item \ "geo_city").extract[String],
			n_plmn = (item \ "x_htc_network_plmn").extract[String],
			o_plmn = (item \ "x_htc_operator_plmn").extract[String],
			time = (item \ "_messagetime").extract[Long]
		)
	}

	def parseCategory(item : json4s.JValue) : CategoryLog = {
		CategoryLog(
			cids = (item \ "cids").extract[String].split(',').toList,
			geo_country = (item \ "geo_country").extract[String],
			geo_region = (item \ "geo_region").extract[String],
			geo_city = (item \ "geo_city").extract[String],
			n_plmn = (item \ "x_htc_network_plmn").extract[String],
			o_plmn = (item \ "x_htc_operator_plmn").extract[String],
			time = (item \ "_messagetime").extract[Long]
		)
	}

	def parseLocation(item : json4s.JValue) : LocationInfo = {
		LocationInfo(
			geo_country = (item \ "geo_country").extract[String],
			geo_region = (item \ "geo_region").extract[String],
			geo_city = (item \ "geo_city").extract[String],
			lastTime = (item \ "_max").extract[String].toFloat.toLong,
			count = (item \ "_count").extract[Int]
		)
	}

	def parseOperator(item : json4s.JValue) : OperatorInfo = {
		OperatorInfo(
			n_plmn = (item \ "x_htc_network_plmn").extract[String],
			o_plmn = (item \ "x_htc_operator_plmn").extract[String],
			lastTime = (item \ "_max").extract[String].toFloat.toLong,
			count = (item \ "_count").extract[Int]
		)
	}

}