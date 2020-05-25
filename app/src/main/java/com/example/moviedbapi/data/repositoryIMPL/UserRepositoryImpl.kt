package com.example.moviedbapi.data.repositoryIMPL

import com.example.moviedbapi.data.models.AccountData
import com.example.moviedbapi.data.network.MovieApi
import com.google.gson.JsonObject
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Response

interface UserRepository {

    fun createToken(): Observable<Response<JsonObject>>

    fun createSession(): Single<Response<JsonObject>>

    fun login (username: String, password: String) : Observable<Boolean>

    fun getAccountDetails(sessionId: String?): Single<AccountData?>
}


class UserRepositoryImpl(private val movieApi: MovieApi) : UserRepository {
    private var requestToken: String? = null

    override  fun createToken(): Observable<Response<JsonObject>> {
        return Observable.create{
            movieApi.createRequestToken()
        }

    }

    override  fun login(username: String, password: String) : Observable<Boolean> {
        val body = JsonObject().apply {
            addProperty("username", username)
            addProperty("password", password)
            addProperty("request_token", requestToken)
        }
        val loginResponse = movieApi.login(body) as Response<JsonObject>
        return Observable.create{
            loginResponse.body()?.getAsJsonPrimitive("success")?.asBoolean ?: false
        }
    }

    override  fun createSession(): Single<Response<JsonObject>> {
        val body = JsonObject().apply {
            addProperty("request_token", requestToken)
        }
        return Single.create {
            movieApi.createSession(body)
        }
    }

    override  fun getAccountDetails(sessionId: String?): Single<AccountData?> {
        return Single.create {emitter ->
            sessionId?.let { movieApi.getAccountId(it).getCompleted().body() }
        }
    }
}