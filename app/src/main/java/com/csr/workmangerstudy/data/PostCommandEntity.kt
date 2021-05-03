package com.csr.workmangerstudy.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PostCommandEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "post_title") val postTitle: String,
    @ColumnInfo(name = "post_body") val postBody: String,
    val type: PostCommandType
)