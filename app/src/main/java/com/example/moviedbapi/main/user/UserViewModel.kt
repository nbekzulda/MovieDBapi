package com.example.moviedbapi.main.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.moviedbapi.additonal.ConnectionFailedException
import com.example.moviedbapi.additonal.launchSafe
import com.example.moviedbapi.base.ParentViewModel
import com.example.moviedbapi.data.models.AccountData
import com.example.moviedbapi.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserViewModel(private val userRepository: UserRepository): ParentViewModel() {
    private val _liveData = MutableLiveData<State>()
    val liveData: LiveData<State>
        get() = _liveData

    fun getAccountDetails(sessionId: String) {
        _liveData.value =
            State.ShowLoading
        uiScope.launchSafe(::handleError) {
            val result = withContext(Dispatchers.IO) {
                userRepository.getAccountDetails(sessionId)
            }
            _liveData.postValue(
                State.Result(result)
            )
        }
        _liveData.value =
            State.HideLoading

    }

    sealed class State {
        object ShowLoading: State()
        object HideLoading: State()
        data class Result(val account: AccountData?): State()
        data class Error(val error: String?): State()
        data class IntError(val error: Int): State()
    }

    override fun handleError(e: Throwable) {
        _liveData.value = State.HideLoading
        if (e is ConnectionFailedException) {
            _liveData.value = State.IntError(e.messageInt)
        } else {
            _liveData.value = State.Error(e.localizedMessage)
        }
    }

}
