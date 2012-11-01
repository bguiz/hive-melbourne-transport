package co.melbournetransport

import org.codehaus.jackson.JsonGenerator
import org.codehaus.jackson.Version
import org.codehaus.jackson.annotate.JsonAutoDetect
import org.codehaus.jackson.map.JsonSerializer
import org.codehaus.jackson.map.SerializerProvider
import org.codehaus.jackson.map.annotate.JsonCachable
import org.codehaus.jackson.map.module.SimpleModule

import com.fasterxml.jackson.annotation.JsonAutoDetect

object CustomJson extends com.codahale.jerkson.Json {
  import org.codehaus.jackson.map.module.SimpleModule
  import org.codehaus.jackson.Version
  val module = new SimpleModule("CustomJson", Version.unknownVersion())
  module.addSerializer(classOf[TripLeg], new TripLegSerializer)
  mapper.registerModule(module)
  module.addSerializer(classOf[Trip], new TripSerializer)
  mapper.registerModule(module)
}

@JsonCachable
class TripLegSerializer extends JsonSerializer[TripLeg] {
  def serialize(id: TripLeg, json: JsonGenerator, provider: SerializerProvider) {
    json.writeString(id.toString)
  }
}

@JsonCachable
class TripSerializer extends JsonSerializer[Trip] {
  def serialize(id: Trip, json: JsonGenerator, provider: SerializerProvider) {
    json.writeString(id.toString)
  }
}