package com.csr.workmangerstudy.data.db

import com.csr.workmangerstudy.data.PostCommandEntity

class LocalDataRepository(private val localDataSource: LocalDataRemoteDataSource) {

    suspend fun getAll(): List<PostCommandEntity> {
        return localDataSource.getAll()
    }

    suspend fun insertPost(postCommand: PostCommandEntity): Result<Boolean> {
        return localDataSource.doInsertPost(postCommand)
    }
}