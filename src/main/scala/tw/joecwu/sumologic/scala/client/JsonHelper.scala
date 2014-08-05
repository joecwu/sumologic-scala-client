package tw.joecwu.sumologic.scala.client

import scala.util.parsing.json.JSONFormat
import com.ning.http.client.Response
import org.json4s._
import org.json4s.native.JsonMethods._
import java.util.zip.GZIPInputStream

object JsonHelper{
  implicit def stringToJsonStr(v:String) = new {
    def toJsonStr = if(v!=null) "\""+JSONFormat.quoteString(v)+"\"" else "null"
  }
}

object GzipJson extends (Response => JValue) {
  def apply(r: Response) = {
    if(r.getHeader("content-encoding")!=null && r.getHeader("content-encoding").equals("gzip")){
      (parse(new GZIPInputStream(r.getResponseBodyAsStream), true))
    }else
      (dispatch.as.String andThen (s => parse(StringInput(s), true)))(r)
  }
}

