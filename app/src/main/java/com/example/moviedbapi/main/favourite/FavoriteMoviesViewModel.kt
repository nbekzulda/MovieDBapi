package com.example.moviedbapi.main.favourite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.moviedbapi.additonal.ConnectionFailedException
import com.example.moviedbapi.additonal.launchSafe
import com.example.moviedbapi.base.ParentViewModel
import com.example.moviedbapi.data.models.MovieData
import com.example.moviedbapi.repository.MovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FavoriteMoviesViewModel( private val movieRepository: MovieRepository) : ParentViewModel(){

    private val _liveData = MutableLiveData<State>()
    val liveData: LiveData<State>
        get() = _liveData

    override fun handleError(e: Throwable) {
        _liveData.value = State.HideLoading
        if (e is ConnectionFailedException) {
            _liveData.value = State.IntError(e.messageInt)
        } else {
            _liveData.value = State.Error(e.localizedMessage)
        }
    }

    fun loadFavMovies(accountId: Int?, sessionId: String?, page: Int = 1) {
        uiScope.launchSafe(::handleError) {
            if (page == 1) {
                _liveData.value = State.ShowLoading
            }

            val result = withContext(Dispatchers.IO) {
                val response =
                    accountId?.let { accountId ->
                        sessionId?.let { admissionID ->
                            movieRepository.getFavoriteMovies(accountId, admissionID, page)
                        }
                    }
                val list = response?.results ?: emptyList()
                val totalPages = response?.totalPages ?: 0
                Pair(totalPages, list)
            }
            _liveData.postValue(
                State.Result(
                    totalPages = result.first,
                    list = result.second
            ))
            _liveData.value = State.HideLoading
        }
    }

    sealed class State {
        object ShowLoading: State()
        object HideLoading: State()
        data class Result(val totalPages: Int, val list: List<MovieData>): State()
        data class Error(val error: String?): State()
        data class IntError(val error: Int): State()
    }
}

