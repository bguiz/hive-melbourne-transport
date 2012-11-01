
package co.melbournetransport
import org.jsoup.select._
import org.jsoup.nodes._
import org.jsoup.Jsoup
import scala.collection.mutable.HashMap
import scala.collection.JavaConversions._
import RegexUtils._
import scala.collection.immutable.SortedMap

object traverser {

  def main(args: Array[String]) {

    var resultsHtml = scala.io.Source.fromFile("tripresult2.html").mkString
    var doc2 = Jsoup.parse(resultsHtml)
    var tripLegs = parseTripLegsFromTripResult(doc2, -1)

    println(tripLegs)

    var listingHtml = scala.io.Source.fromFile("triplisting.html").mkString

    var doc = Jsoup.parse(listingHtml)
    var lis = doc.getElementsByTag("li")

    lis.foreach(li => {
      println(getTripDetail(li))
    })

  }

  def parseTripLegsFromTripResult(doc: Document, tripIdx: Int): SortedMap[Int, TripLeg] = {
    //get all tr with a class that ends with '_results', exclude invisible ones

    var tableRows = doc.select("tr[class$=_results]:not([style*=none])")
    //println(tableRows)
    var tripLegs = SortedMap.empty[Int, TripLeg]

    var count = 0
    tableRows.view.zipWithIndex.foreach {
      case (row, rowIdx) => {
        var currentClass = row.attr("class")
        var getNthTd = (i: Int) => getNthTdFromEl(row, i)

        //row.select("td:eq(%d)".format(i))

        //var currentTrip = () => tripLegs.get(currentClass)
        tripLegs.get(count) match {
          //key exits already
          case Some(tripLeg) => {

            (rowIdx % 3) match {
              //case 0 => {
              //  //does not exist, yet, created and populated at
              //  //"create tripLeg here, upon first encounter of a new row"
              //}
              case 1 => {
                var tdElement = getNthTdFromEl(row, 3)

                println("tripIdx=" + tripIdx +
                  " rowIdx=" + rowIdx //+
                  //" tdElement.textNodes=" + tdElement.textNodes
                  ); //DEBUG output
                tdElement.textNodes.foreach {
                  node =>
                    {
                      println("node.text.trim=" + node.text.trim);
                      node.text.trim match {
                        case text if (TimeRegex matches text) => {
                          val TimeRegex(time) = text;
                          tripLeg.time = time;
                        }
                        case text if (ZoneRegex matches text) => {
                          val ZoneRegex(zone) = text;
                          tripLeg.zone = zone;
                        }
                        case text if (OperatorRegex matches text) => {
                          val OperatorRegex(operator) = text;
                          tripLeg.operator = operator;
                        }
                        case text if (FrequencyRegex matches text) => {
                          var FrequencyRegex(frequency) = text;
                          tripLeg.frequency = frequency;
                        }
                        case text if (OptionRegex matches text) => {
                          println(text);
                        }
                        case text => {
                          if (!tripLeg.instruction.isEmpty) tripLeg.instruction += "|!|";
                          tripLeg.instruction += text;
                        }
                      }
                    }
                }

                tripLegs += count -> tripLeg
              }
              case 2 => {
                var tdElement = getNthTdFromEl(row, 3)
                var arrivalLocation = tdElement.select("strong + a").text()

                if (arrivalLocation.isEmpty) {
                  var node = tdElement.textNodes().toList.filter(item => !item.text.trim.isEmpty)
                  if (!node.isEmpty)
                    arrivalLocation = node.get(0).text()
                }

                tripLeg.arrivalLocation = arrivalLocation
                //tripLeg.instruction += " | " + arrivalLocation;

                tripLegs += count -> tripLeg
                count += 1
              }
              case _ => {
                //Do nothing
              }
            }
          }
          case None => {
            //create tripLeg here, upon first encounter of a new row
            val tripMode = row.select("td img").first().attr("alt")
            val departureTime = getNthTdFromEl(row, 2).text()
            val tripLegStartLocation = getNthTd(3).text()
            //val tripInstruction = tripLegStartLocation;
            val tripInstruction = "";
            // get the image mode
            tripLegs += (count -> TripLeg(tripMode, departureTime, "", tripInstruction, "", tripLegStartLocation, "", "", ""))
          }
          case _ => {
            //Do nothing
          }
        }
      }
    }
    tripLegs
  }

  /*Returns a tuple of trip details with
    * _1 = departureTime
    * _2 = arrivalTime
    * _3 = duration*/
  def getTripDetail(li: Element): (String, String, String) = {

    val tripDivs = li.select("a div[style=float:left;]").first.children;
    var departureTime = ""
    var arrivalTime = ""
    var duration = ""

    tripDivs.zipWithIndex.foreach {
      case (child, index) => {
        child.text match {
          case text if (DepartRegex matches text) => {
            departureTime = tripDivs.get(index + 1).text;
          }
          case text if (ArriveRegex matches text) => {
            arrivalTime = tripDivs.get(index + 1).text;
          }
          case text if (DurationRegex matches text) => {
            duration = tripDivs.get(index + 1).text;
          }
          case text => {
            //Do nothing
          }
        }
      }
    }
    (departureTime, arrivalTime, duration)
  }

  def getNthTdFromEl(el: Element, index: Int): Element = {
    el.select("td:eq(%d)".format(index)).first()
  }

}
