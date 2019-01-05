package com.github.lubang.example.todolist.port.adapter.serialization

import com.google.gson.*
import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat

object GsonUtility {
    fun configure(gsonBuilder: GsonBuilder) {
        gsonBuilder
            .registerTypeAdapter(
                DateTime::class.java,
                JsonSerializer<DateTime> { src, _, _ ->
                    JsonPrimitive(ISODateTimeFormat.dateTime().print(src))
                })
            .registerTypeAdapter(
                DateTime::class.java,
                JsonDeserializer<DateTime> { src, _, _ ->
                    ISODateTimeFormat.dateTime().parseDateTime(src.asString)
                })
            .create()
    }

    fun createInstance(): Gson {
        val gsonBuilder = GsonBuilder()
        configure(gsonBuilder)
        return gsonBuilder.create()
    }
}