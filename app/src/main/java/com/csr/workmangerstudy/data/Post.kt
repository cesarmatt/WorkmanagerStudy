package com.csr.workmangerstudy.data

import com.google.gson.Gson


class Post(val id: String? = null, val postTitle: String? = null, val postBody: String? = null) {

    companion object {
        val gson = Gson()
    }

    fun serializeToJson(post: Post): String {
        return gson.toJson(post)
    }

    fun deserializeFromJson(postAsJson: String): Post {
        return gson.fromJson(postAsJson, Post::class.java)
    }
}