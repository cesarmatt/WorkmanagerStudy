package com.csr.workmangerstudy.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PostDao {

    @Query("SELECT * FROM postcommandentity")
    suspend fun getAll(): List<PostCommandEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(postCommandEntity: PostCommandEntity)
}