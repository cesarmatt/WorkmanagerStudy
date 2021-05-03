package com.csr.workmangerstudy.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.csr.workmangerstudy.Constants.KEY_POST
import com.csr.workmangerstudy.data.ApiRepository
import com.csr.workmangerstudy.data.Post
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import timber.log.Timber

class UploadWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params), KoinComponent {

    val repository: ApiRepository by inject()

    override suspend fun doWork(): Result {
        val postAsJson = inputData.getString(KEY_POST)

        val post = postAsJson?.let { Post().deserializeFromJson(it) }

        return try {
            val response = post?.let {
                Timber.i("Uploading ${it.postTitle}")
                repository.sendPost(it)
            }
            val outputData = workDataOf(KEY_POST to response)
            Result.success(outputData)
        } catch (throwable: Throwable) {
            Result.failure()
        }
    }
}