package com.example.moviedbapi.main.authorization

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.moviedbapi.base.ParentViewModel
import com.example.moviedbapi.data.models.AccountData
import com.example.moviedbapi.data.repositoryIMPL.UserRepository
import com.google.gson.JsonObject
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*
import retrofit2.Response
import kotlin.coroutines.CoroutineContext

class AuthorizationViewModel (private val userRepository: UserRepository) : ParentViewModel() {

    private val _liveData = MutableLiveData<State>()

    val liveData: LiveData<State>
        get() = _liveData

    private val job = SupervisorJob()

    private val coroutineContext: CoroutineContext = Dispatchers.Main + job

    private val uiScope: CoroutineScope = CoroutineScope(coroutineContext)


    fun login(username: String, password: String) {


        _liveData.value = State.ShowLoading

        val disposable1: Disposable = userRepository.createToken()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()

        val disposable2: Disposable = userRepository.login(username, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()

        val createToken = userRepository.createToken()
        val log =  userRepository.login(username, password)
        val resp = arrayOf(createToken, log) as Boolean


        val disposable3: Disposable = userRepository.createSession()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()

        val sesID = userRepository.createSession() as Response<JsonObject>
        val sessionID = sesID.body()?.getAsJsonPrimitive("session_id")?.asString

        val disposable4: Disposable = userRepository.getAccountDetails(sessionID)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()

        val accountId = userRepository.getAccountDetails(sessionID) as Response<AccountData>
        val userDataId = accountId.body()?.id

        _liveData.value = State.HideLoading

        disposables.addAll(disposable1, disposable2, disposable3, disposable4)

        _liveData.postValue(sessionID?.let { State.ApiResult(resp, it, userDataId) })
    }

    sealed class State{
        object ShowLoading: State()
        object HideLoading: State()
        data class ApiResult(val success: Boolean, val session_id: String, val account_id: Int?): State()
    }



}