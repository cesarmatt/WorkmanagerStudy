package com.csr.workmangerstudy.data.db

import androidx.room.TypeConverter
import com.csr.workmangerstudy.data.PostCommandType

class Converters {
    @TypeConverter
    fun toPostCommandType(value: String) = enumValueOf<PostCommandType>(value)

    @TypeConverter
    fun fromPostCommandType(value: PostCommandType) = value.name
}