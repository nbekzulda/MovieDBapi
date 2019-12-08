package com.example.moviedbapi.main.cinema
import androidx.lifecycle.LiveData
import com.example.moviedbapi.additonal.ConnectionFailedException
import com.example.moviedbapi.base.ParentViewModel
import com.example.moviedbapi.data.repositoryIMPL.CinemaRepositoryImpl
import com.example.moviedbapi.data.roomCinema.Cinema
import com.example.moviedbapi.data.roomCinema.CinemaDao
import com.example.moviedbapi.repository.CinemaRepository

class CinemaViewModel(private val cinemaDao: CinemaDao) : ParentViewModel() {

    private val repository: CinemaRepository

    var liveData: LiveData<List<Cinema>>

    init {

        repository = CinemaRepositoryImpl(cinemaDao)
        liveData = repository.getAllCinemas()
    }
    override fun handleError(e: Throwable) {
        if (e is ConnectionFailedException) {
            //ToDo
        }
    }
}