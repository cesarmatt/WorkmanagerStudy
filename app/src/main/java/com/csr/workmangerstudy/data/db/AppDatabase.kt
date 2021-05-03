package com.csr.workmangerstudy.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.csr.workmangerstudy.data.PostCommandEntity
import com.csr.workmangerstudy.data.PostDao

@Database(
    entities = [
        PostCommandEntity::class
    ],
    version = 3
)

@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun postDao(): PostDao

    companion object {
        private const val databaseName = "post-db"

        fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, databaseName).build()
        }
    }
}