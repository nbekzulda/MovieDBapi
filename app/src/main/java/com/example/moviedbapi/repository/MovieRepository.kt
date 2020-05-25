package com.example.moviedbapi.repository

import com.example.moviedbapi.data.models.MovieData
import com.example.moviedbapi.data.models.MovieResponseData
import io.reactivex.Observable
import io.reactivex.Single

interface MovieRepository {

     fun getPopularMovies(page: Int) : Observable<MovieResponseData?>

     fun getMovieById(movieId: Int): Single<MovieData>

     fun getFavoriteMovies(accountId: Int?, sessionId: String?, page: Int): Observable<MovieResponseData?>

     fun rateMovie(movieId: Int, accountId: Int, sessionId: String, favorite: Boolean): Single<Int?>

     fun getState(movieId: Int, accountId: Int, sessionId: String, favoriteState: Boolean): Single<Int?>

}