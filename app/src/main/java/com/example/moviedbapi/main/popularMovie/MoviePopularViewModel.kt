package com.example.moviedbapi.main.popularMovie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.moviedbapi.additonal.ConnectionFailedException
import com.example.moviedbapi.base.ParentViewModel
import com.example.moviedbapi.data.models.MovieData
import com.example.moviedbapi.data.models.MovieResponseData
import com.example.moviedbapi.main.favourite.FavoriteMoviesViewModel
import com.example.moviedbapi.repository.MovieRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MoviePopularViewModel(private val movieRepository: MovieRepository) : ParentViewModel() {

    private val _liveData = MutableLiveData<State>()
    val liveData: LiveData<State>
        get() = _liveData

    init {
        loadMovies()
    }

     fun loadMovies(page: Int = 1) {


        disposables.add(
            movieRepository.getPopularMovies(page)
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
                .subscribe {
                    val result = {
                        val response = movieRepository.getPopularMovies(
                            page
                        ) as MovieResponseData
                        val list = response?.results ?: emptyList()
                        val totalPages = response?.totalPages ?: 0
                        Pair(totalPages, list)
                    } as Pair<Int, List<MovieData>>
                    _liveData.postValue(State.Result(totalPage = result.first, list = result.second ))
                }
        )
    }

    sealed class State {
        object ShowLoading: State()
        object HideLoading: State()
        data class Result(val totalPage: Int, val list: List<MovieData>): State()
        data class Error(val error: String?): State()
        data class IntError(val error: Int): State()
    }
}