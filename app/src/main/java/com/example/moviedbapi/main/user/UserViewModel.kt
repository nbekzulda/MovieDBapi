package com.example.moviedbapi.main.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.moviedbapi.additonal.ConnectionFailedException
import com.example.moviedbapi.base.ParentViewModel
import com.example.moviedbapi.data.models.AccountData
import com.example.moviedbapi.data.repositoryIMPL.UserRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserViewModel(private val userRepository: UserRepository): ParentViewModel() {
    private val _liveData = MutableLiveData<State>()
    val liveData: LiveData<State>
        get() = _liveData

    fun getAccountDetails(sessionId: String) {

        disposables.add(
            userRepository.getAccountDetails(sessionId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                _liveData.value =
                    State.ShowLoading
            }
            .doFinally {
                _liveData.value =
                    State.HideLoading
            }
            .subscribe({result ->
                _liveData.postValue((State.Result(result)))
            }, {
                if (it is ConnectionFailedException) {
                    _liveData.value = State.IntError(it.messageInt)
                } else {
                _liveData.value = State.Error(it.localizedMessage)
                }
            })
        )



    }

    sealed class State {
        object ShowLoading: State()
        object HideLoading: State()
        data class Result(val account: AccountData?): State()
        data class Error(val error: String?): State()
        data class IntError(val error: Int): State()
    }



}
