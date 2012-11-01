package co.melbournetransport
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.codahale.jerkson.Json._

@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.ANY)
case class TripLeg(var tripMode: String, 
    		  		var departureTime:String, 
    		  		var arrivalLocation: String,
    		  		var instruction:String,
    		  		var time: String, //duration
    		  		var tripLegStartLocation: String,
    		  		var frequency: String,
    		  		var zone: String,
    		  		var operator: String)
