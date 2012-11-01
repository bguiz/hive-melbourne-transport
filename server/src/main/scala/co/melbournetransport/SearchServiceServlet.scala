/**
 *
 */
package co.melbournetransport

import util.control.Exception.allCatch
import org.scalatra._
import scalate.ScalateSupport
import scala.collection.mutable.{ Queue, HashMap }
import org.jsoup.Jsoup
import org.jsoup.select._
import org.jsoup.nodes._
import scala.collection.JavaConversions._
import scala.collection.immutable.SortedMap
import traverser._
import scala.tools.nsc.doc.model.Visibility
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.codahale.jerkson.Json._

import java.text.SimpleDateFormat
import java.util.Date
/**
 * @author mebinum
 *
 */
class SearchServiceServlet extends ScalatraServlet with ScalateSupport {

  val jpBaseURI = "http://jp.ptv.vic.gov.au/iphone/XSLT_TRIP_REQUEST2"
  //0 long: 1 lat :  2address
  val addressFormat = "%s:%s:WGS84[DD.ddddd]:%s";
  val requestMap = SortedMap(
    "language" -> "en",
    "sessionID" -> "0",
    "exclMOT_11" -> "1",
    "exclMOT_9" -> "1",
    "itdTripDateTimeDepArr" -> "dep",
    "excludedMeans" -> "checkbox",
    "ptOptionsActive" -> "1",
    "type_destination" -> "coord",
    "type_origin" -> "coord",
    "useProxFootSearch" -> "1",
    "useProxFootSearchDestination" -> "1",
    "useProxFootSearchOrigin" -> "1")

  before() {
    contentType = "application/javascript"
  }

  get("/") {
    redirect("/search/v0.1/do.js")
  }

  get("/search/v0.1/do.js") {
    import java.net.URLEncoder
    try {
      val origin = params("oname")
      val destination = params("dname")
      val startTime = params("dtime")
      val fullRequest = requestMap + ("name_destination" -> getPtLocation(params("dlong"), params("dlat"), destination),
        "itdDate" -> params("ddate"),
        "itdTime" -> params("dtime"),
        "name_origin" -> getPtLocation(params("olong"), params("olat"), origin))

      val fullUrl = jpBaseURI + fullRequest.map(pair => pair._1 + "=" + URLEncoder.encode(pair._2, "UTF-8")).mkString("?", "&", "#:___1__")
      println(fullUrl)

      getTrips(fullUrl) match {
        case Some(trips) => {
          val result = new TripResult(origin, destination, startTime, trips)
          val callback = params.getOrElse("callback", "callback")
          callback + "(" + generate(result) + ");"
        }
        case None => error("no results", "we didn't get any results for that search")
      }
    } catch {
      case e: java.util.NoSuchElementException =>
        {
          session("emessage") = e.toString
          redirect("/error")
        }
    }
  }

  get("/error") {
    error("invalid request",
      session("emessage") match {
        case Some(msg: String) => msg
        case _ => "An error occured"
      })
  }

  def error(message: String, errormsg: String) {
    params.getOrElse("callback", "callback") + "(" + """{"result":"0","message":"%s","errormsg":"%s}"""".format(message, errormsg) + ");"
  }

  def getPtLocation(long: String, lat: String, name: String) = {
    addressFormat.format(long, lat, name)
  }

  def getTrips(jpUrl: String): Option[List[Trip]] = {
    import java.net.URLEncoder

    val doc = Jsoup.connect(jpUrl).get()
    val results = doc.getElementsByTag("li")
    var trips = List.empty[Trip]

    if (results.isEmpty()) None

    var matchLiCount = 0;
    results.view.zipWithIndex.foreach {
      case (li, index) => {
        //get trip results page url and get the results
        val anchor = li.select("a[href]").first()

        anchor match {
          case doc: Element => {
            //TODO: Make Asynchronous while the result page is being retrieved
            //the trip detail page can be parsed
            var document = getPage(doc)
            if (document.isDefined) {
              matchLiCount += 1;
              var trip = getTripDetail(li)
              val legs = traverser.parseTripLegsFromTripResult(document.get, matchLiCount).values.toList
              trips = trips ::: List(Trip(index, trip._1, trip._2, trip._3, "", legs))
            }
          }
          case _ => return None
        }
      }
    }

    Some(trips)
  }

  def getPage(link: Element): Option[Document] = {
    var linkUrl = jpBaseURI + link.attr("href")
    Some(Jsoup.connect(linkUrl).get())
  }

  notFound {
    // Try to render a ScalateTemplate if no route matched
    findTemplate(requestPath) map { path =>
      contentType = "text/html"
      layoutTemplate(path)
    } orElse serveStaticResource() getOrElse resourceNotFound()
  }
}