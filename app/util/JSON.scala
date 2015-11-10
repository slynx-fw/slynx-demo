import util.{JSON, JsonSerializable}

package com.fasterxml.jackson.module.scala.modifiers {

import com.fasterxml.jackson.module.scala.JacksonModule
import util.JsonSerializable

private object JsonSerializableTypeModifier extends CollectionLikeTypeModifier {
  def BASE = classOf[JsonSerializable]
}

trait JsonSerializableTypeModifierModule extends JacksonModule {
  this += JsonSerializableTypeModifier
}

}

package util {

import java.{util => ju}

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind._
import com.fasterxml.jackson.databind.ser.Serializers
import com.fasterxml.jackson.module.scala.JacksonModule

trait JsonSerializable {
  def json(): Option[String]
}

case class RawJSON(code: String) extends JsonSerializable {
  override def json(): Option[String] = Some(code)
}

private class JsonSerializableSerializer extends JsonSerializer[JsonSerializable] {

  def serialize(value: JsonSerializable, jgen: JsonGenerator, provider: SerializerProvider) {
    value.json().foreach(v => jgen.writeRawValue(v))
  }
}

private object JsonSerializableSerializerResolver extends Serializers.Base {

  override def findSerializer(config: SerializationConfig, javaType: JavaType, beanDesc: BeanDescription) = {
    val cls = javaType.getRawClass
    if (!classOf[JsonSerializable].isAssignableFrom(cls)) null
    else new JsonSerializableSerializer
  }

}

trait JsonSerializableSerializerModule extends JacksonModule {
  this += (_ addSerializers JsonSerializableSerializerResolver)
}

import com.fasterxml.jackson.module.scala._


object JSON extends ObjectMapper {


  registerModule(DefaultScalaModule)
  registerModule(new JsonSerializableSerializerModule {})
  setSerializationInclusion(JsonInclude.Include.NON_NULL)
  enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL)

  val L = new ObjectMapper {

    setSerializationInclusion(JsonInclude.Include.NON_NULL)
    registerModule(DefaultScalaModule)
    registerModule(new JsonSerializableSerializerModule {})

  }

}

}


object Main extends App {
  println(JSON.writeValueAsString(List(1, 2, new JsonSerializable {
    def json(): Option[String] = Some("new Date()")
  })))
}