package com.csr.workmangerstudy.create

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csr.workmangerstudy.data.ApiRepository
import com.csr.workmangerstudy.data.PostCommandEntity
import com.csr.workmangerstudy.data.PostCommandType
import com.csr.workmangerstudy.data.db.LocalDataRepository
import com.csr.workmangerstudy.data.db.Result
import kotlinx.coroutines.launch
import java.util.concurrent.ThreadLocalRandom
import kotlin.streams.asSequence

class CreateViewModel(private val localDataRepository: LocalDataRepository) : ViewModel() {

    private val _uiState = MutableLiveData<CreateUiState>()
    val uiState: LiveData<CreateUiState>
        get() = _uiState

    fun save(title: String, body: String) {
        viewModelScope.launch {
            emitUiState(true)
            val command = makeCommand(title, body)
            val result = localDataRepository.insertPost(command)

            if (result is Result.Success) {
                emitUiState(false)
            }
        }
    }

    private fun makeCommand(title: String, body: String): PostCommandEntity {
        return PostCommandEntity(
            id = generateRandomString(),
            postTitle = title,
            postBody = body,
            type = PostCommandType.CREATE
        )
    }

    private fun emitUiState(loading: Boolean = false) {
        _uiState.value = CreateUiState(loading)
    }

    private fun generateRandomString(): String {
        val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

        val randomString = (1..10)
            .map { i -> kotlin.random.Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("");

        return randomString
    }
}

class CreateUiState(
    val loading: Boolean
)

