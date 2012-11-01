package co.melbournetransport 
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.codahale.jerkson.Json._

@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.ANY)
   case class TripResult(origin: String, 
    		  		destination:String, 
    		  		tripStartTime: String,
    		  		trips:List[Trip])