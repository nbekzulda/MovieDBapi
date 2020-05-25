package com.example.moviedbapi.main.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.moviedbapi.additonal.ConnectionFailedException
import com.example.moviedbapi.base.ParentViewModel
import com.example.moviedbapi.data.models.MovieData

import com.example.moviedbapi.repository.MovieRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MovieInfoViewModel(private val movieRepository: MovieRepository) : ParentViewModel() {

    private val _liveData = MutableLiveData<State>()
    val liveData: LiveData<State>
        get() = _liveData

    fun getMovie(movieId: Int) {

        disposables.add(
            movieRepository.getMovieById(movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    _liveData.value = State.ShowLoading
                }
                .doFinally {
                    _liveData.value = State.HideLoading
                }
                .subscribe()
        )
    }

    fun setFavorite(accountId: Int, movieId: Int, sessionId: String,setFav: Boolean) {

        disposables.add(
            movieRepository.rateMovie(movieId, accountId, sessionId, favorite = setFav)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({data->
                    _liveData.postValue(State.FavoriteMovie(data))
                }, {
                    _liveData.value = State.Error(it.message.toString())
                })
        )
    }

    fun getMovieStatus(accountId: Int, movieId: Int, sessionId: String, setFavState: Boolean) {

        disposables.add(
            movieRepository.getState(movieId, accountId, sessionId,setFavState)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({data ->
                    _liveData.postValue(State.MovieState(data))
                }, {
                    _liveData.value = State.Error(it.message.toString())
                })
        )
    }



    sealed class State {
        object ShowLoading: State()
        object HideLoading: State()
        data class Result(val movie: MovieData): State()
        data class MovieState(val resultCode: Int?): State()
        data class FavoriteMovie(val resultCode: Int?): State()
        data class Error(val error: String?): State()
        data class IntError(val error: Int): State()
    }
}