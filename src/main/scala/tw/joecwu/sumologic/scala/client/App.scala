package tw.joecwu.sumologic.scala.client

import com.sumologic.client._
import com.sumologic.client.model._
import scala.collection.JavaConversions._
import scala.concurrent.{TimeoutException, Future, ExecutionContext}
import java.util.concurrent.Executors
import java.util.{Date, Calendar}
import java.net.URLEncoder


object DashboardApiApp extends Logger {
	implicit val ec = ExecutionContext.fromExecutorService(Executors.newCachedThreadPool())

	val DEVICE_SN = "TEST_DSN"

	def main(args: Array[String]) {
		log.info("Start.")

		val st = Calendar.getInstance()
		val et = Calendar.getInstance()
		st.add(Calendar.DAY_OF_YEAR, -5)
/* 
		// Search Articles
		val articles = SumologicHelper.searchArticles(DEVICE_SN,st.getTime,et.getTime)
		articles.map{ a => 
			log.debug("Size:"+a.size)
			a.map{ item => 
				println(s"${item.time}\t${item.aid}\t${item.geo_country}\t${item.o_plmn}")
			}
		}
*/

/*
		// Search Categories
		val categories = SumologicHelper.searchCategories(DEVICE_SN,st.getTime,et.getTime)
		categories.map{ c =>
			log.debug("Size:"+c.size)
			c.map{ item =>
				println(s"${item.time}\t${item.cids.mkString(",")}\t${item.geo_country}\t${item.o_plmn}")
			}
		}
*/

/*
		// Search Locations
		val locations = SumologicHelper.searchLocations(DEVICE_SN,st.getTime,et.getTime)
		locations.map{ l =>
			log.debug("Size:"+l.size)
			l.map{ item =>
				println(s"${item.geo_country}\t${item.geo_region}\t${item.geo_city}\t${item.lastTime}\t${item.count}")
			}
		}
*/

		// Search Operators
		val operators = SumologicHelper.searchOperators(DEVICE_SN,st.getTime,et.getTime)
		operators.map{ o =>
			log.debug("Size:"+o.size)
			o.map{ item =>
				println(s"${item.o_plmn}\t${item.n_plmn}\t${item.lastTime}\t${item.count}")
			}
		}

		log.info("Done.")
	}

}