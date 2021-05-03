package com.csr.workmangerstudy.data.db

import com.csr.workmangerstudy.data.PostCommandEntity
import com.csr.workmangerstudy.data.PostDao

class LocalDataRemoteDataSource(private val dao: PostDao) {

    suspend fun doGetAll() = doQuery(
        query = { getAll() },
        errorMessage = "Could not get all posts"
    )

    suspend fun doInsertPost(postCommand: PostCommandEntity) = doQuery(
        query = { insertPost(postCommand) },
        errorMessage = "Could not create your post"
    )

    suspend fun getAll(): List<PostCommandEntity> {
        return dao.getAll()
    }

    private suspend fun insertPost(postCommand: PostCommandEntity): Boolean {
        dao.insertPost(postCommand)
        return true
    }
}