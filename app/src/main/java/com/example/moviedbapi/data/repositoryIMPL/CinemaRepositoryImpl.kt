package com.example.moviedbapi.data.repositoryIMPL

import androidx.lifecycle.LiveData
import com.example.moviedbapi.data.roomCinema.Cinema
import com.example.moviedbapi.data.roomCinema.CinemaDao
import com.example.moviedbapi.repository.CinemaRepository


class CinemaRepositoryImpl(private val cinemaDao: CinemaDao) : CinemaRepository {

    override fun getAllCinemas(): LiveData<List<Cinema>> = cinemaDao.getCinemas()

    override fun getCinema(id: Int): LiveData<Cinema> = cinemaDao.getCinema(id)
}