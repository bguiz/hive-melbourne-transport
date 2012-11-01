/**
 *
 */
package co.melbournetransport
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.codahale.jerkson.Json._

@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.ANY)
case class Trip(id:Int, var departureTime: String, 
    		  		var arrivalTime:String, 
    		  		var duration: String,
    		  		var startTime:String,
    		  		var legs: List[TripLeg])
   

