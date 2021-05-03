package com.csr.workmangerstudy.data

class ApiRepository(private val service: ApiService) {

    suspend fun getPosts(): List<Post>? {
        val response = service.getPosts()
        return response.body()
    }

    suspend fun sendPost(post: Post): Boolean {
        val response = service.sendPost(post)

        return response.isSuccessful
    }
}