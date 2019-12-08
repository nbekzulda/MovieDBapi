package com.example.moviedbapi.repository

import androidx.lifecycle.LiveData
import com.example.moviedbapi.data.roomCinema.Cinema

interface CinemaRepository {

    fun getAllCinemas(): LiveData<List<Cinema>>

    fun getCinema(id: Int): LiveData<Cinema>
}