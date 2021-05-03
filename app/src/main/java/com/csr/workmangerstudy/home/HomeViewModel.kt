package com.csr.workmangerstudy.home

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.*
import com.csr.workmangerstudy.Constants.KEY_POST
import com.csr.workmangerstudy.Constants.TAG_OUTPUT
import com.csr.workmangerstudy.Constants.TAG_PROGRESS
import com.csr.workmangerstudy.Constants.UPLOAD_WORK_NAME
import com.csr.workmangerstudy.create.CreateUiState
import com.csr.workmangerstudy.data.ApiRepository
import com.csr.workmangerstudy.data.db.LocalDataRepository
import com.csr.workmangerstudy.data.Post
import com.csr.workmangerstudy.data.PostCommandEntity
import com.csr.workmangerstudy.workers.UploadWorker
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: ApiRepository,
    private val localDataRepository: LocalDataRepository,
    private val application: Application
) : ViewModel() {

    private val _uiState = MutableLiveData<HomeUiState>()
    val uiState: LiveData<HomeUiState>
        get() = _uiState

    private val _posts = MutableLiveData<List<Post>>()
    val posts: LiveData<List<Post>>
        get() = _posts

    val outputWorkInfoItems: LiveData<List<WorkInfo>>
    val progressWorkInfoItems: LiveData<List<WorkInfo>>
    private val workManager = WorkManager.getInstance(application)

    init {
        getPosts()
        outputWorkInfoItems = workManager.getWorkInfosByTagLiveData(TAG_OUTPUT)
        progressWorkInfoItems = workManager.getWorkInfosByTagLiveData(TAG_PROGRESS)
    }

    private fun getPosts() {
        viewModelScope.launch {
            emitUiState(loading = true)
            val response = localDataRepository.getAll()
            val postList = mutableListOf<Post>()

            for (post in response) {
                postList.add(makePost(post))
            }

            if (response != null) {
                _posts.value = postList
                emitUiState(loading = false)
            }
        }
    }

    fun uploadPosts() {
        _posts.value?.let {
            emitUiState(loading = true)

            for (post in it) {
                val constraints = getConstraints()
                val uploadBuilder = OneTimeWorkRequestBuilder<UploadWorker>()
                    .setConstraints(constraints)

                uploadBuilder.setInputData(makePostData(post)).addTag(TAG_PROGRESS).build()
                val workRequest = uploadBuilder.build()

                workManager.enqueue(workRequest)
            }

            emitUiState(loading = false)
        }
    }

    private fun makePostData(post: Post): Data {
        val builder = Data.Builder()
        val postAsJson: String = Post().serializeToJson(post)

        builder.putString(KEY_POST, postAsJson)

        return builder.build()
    }

    private fun getConstraints(): Constraints {
        return Constraints.Builder()
                          .setRequiredNetworkType(NetworkType.CONNECTED)
                          .build()
    }

    private fun makePost(commandEntity: PostCommandEntity): Post {
        return Post(
            id = commandEntity.id,
            postTitle = commandEntity.postTitle,
            postBody = commandEntity.postBody
        )
    }

    private fun emitUiState(loading: Boolean = false) {
        _uiState.value = HomeUiState(loading)
    }
}

class HomeUiState(
    val loading: Boolean
)