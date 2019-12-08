package com.example.moviedbapi.main.cinema.details
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.moviedbapi.data.repositoryIMPL.CinemaRepositoryImpl
import com.example.moviedbapi.data.roomCinema.Cinema
import com.example.moviedbapi.data.roomCinema.CinemaRoomDatabase
import com.example.moviedbapi.repository.CinemaRepository
import kotlinx.coroutines.launch

class CinemaInfoViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: CinemaRepository

    lateinit var liveData: LiveData<Cinema>

    init {
        val cinemaDao = CinemaRoomDatabase.getDatabase(application, viewModelScope).cinemaDao()
        repository = CinemaRepositoryImpl(cinemaDao)
    }

    fun getCinema(id: Int) {
        viewModelScope.launch {
            val cinema = repository.getCinema(id)
            cinema.let { cinema->
                liveData = cinema
            }
        }
    }
}