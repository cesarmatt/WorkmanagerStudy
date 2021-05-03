package com.csr.workmangerstudy.data.db

import android.util.Log
import java.io.IOException

suspend inline fun <T : Any> doQuery(
    crossinline query: suspend () -> T,
    errorMessage: String
): Result<T> {
    return try {
        val data = query()
        Result.Success(data)
    } catch (e: Exception) {
        Log.e("doQuery", e.localizedMessage, e)
        Result.Error(IOException(errorMessage, e))
    }
}

sealed class Result<out T : Any> {

    data class Success<out T : Any>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
        }
    }
}