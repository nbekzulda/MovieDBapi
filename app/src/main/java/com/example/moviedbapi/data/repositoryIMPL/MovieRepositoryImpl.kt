package com.example.moviedbapi.data.repositoryIMPL

import com.example.moviedbapi.data.models.MovieData
import com.example.moviedbapi.data.models.MovieResponseData
import com.example.moviedbapi.data.network.MovieApi
import com.example.moviedbapi.repository.MovieRepository
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.Response
import org.json.JSONObject

class MovieRepositoryImpl(private val movieApi : MovieApi): MovieRepository {

    override  fun getPopularMovies(page: Int) : Observable<MovieResponseData?> {
       return Observable.create { emitter ->
           movieApi.getPopularMovies(page)
       }
    }

    override  fun getFavoriteMovies(
        accountId: Int?,
        sessionId: String?,
        page: Int
    ): Observable<MovieResponseData?> {
        return Observable.create{emitter ->
            accountId?.let { sessionId?.let { it1 -> movieApi.getFavoriteMovies(it, it1, page) } }
        }
    }


    override fun getMovieById(movieId: Int): Single<MovieData> {
     return Single.create { emitter ->
           movieApi.getMovie(movieId)
     }
    }

    override  fun rateMovie(movieId: Int, accountId: Int, sessionId: String, favorite: Boolean): Single<Int?> {
//        val body = JsonObject().apply {
//            addProperty("media_type", "movie")
//            addProperty("media_id", movieId)
//            addProperty("favorite", favorite)
//        }
//        val response = movieApi.rateMovie(accountId, sessionId, body).await()
//        return response.body()?.getAsJsonPrimitive("status_code")?.asInt
        return Single.create { emitter ->
            val body = JsonObject().apply {
                addProperty("media_type", "movie")
                addProperty("media_id", movieId)
                addProperty("favorite", favorite)
            }
            val response = movieApi.rateMovie(accountId, sessionId, body)
            response.body()?.getAsJsonPrimitive("status_code")?.asInt
        }
    }

    override  fun getState(movieId: Int, accountId: Int, sessionId: String, favoriteState: Boolean): Single<Int?> {
//        val body = JsonObject().apply {
//            addProperty("media_type", "movie")
//            addProperty("media_id", movieId)
//            addProperty("favorite", favoriteState)
//        }
//        val response = movieApi.rateMovie(accountId, sessionId, body).await()
//        return response.body()?.getAsJsonPrimitive("status_code")?.asInt
        return Single.create { emitter ->
            val body = JsonObject().apply {
                addProperty("media_type", "movie")
                addProperty("media_id", movieId)
                addProperty("favorite", favoriteState)
            }
            val response = movieApi.rateMovie(accountId, sessionId, body)
            response.body()?.getAsJsonPrimitive("status_code")?.asInt
        }

    }
}