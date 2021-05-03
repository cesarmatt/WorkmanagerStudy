package com.csr.workmangerstudy

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import java.util.*

class DateJsonAdapter : JsonAdapter<Date>() {

    private val delegate = Rfc3339DateJsonAdapter()

    override fun fromJson(reader: JsonReader): Date? {
        if (reader.peek() == JsonReader.Token.STRING) {
            return delegate.fromJson(reader)
        }
        return try {
            val dateString = reader.nextString() ?: return null
            val dateLong = dateString.toLong()
            Date(dateLong)
        } catch (e: Exception) {
            reader.nextNull()
        }
    }

    override fun toJson(writer: JsonWriter, value: Date?) {
        if (value == null) {
            writer.nullValue()
        } else {
            writer.value(value.time)
        }
    }

}