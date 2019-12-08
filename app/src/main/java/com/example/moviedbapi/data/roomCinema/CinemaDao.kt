package com.example.moviedbapi.data.roomCinema
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CinemaDao {

    @Query("SELECT * FROM cinema_table")
    fun getCinemas(): LiveData<List<Cinema>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(cinema: Cinema)

    @Query("DELETE FROM cinema_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM cinema_table WHERE id=:id")
    fun getCinema(id: Int): LiveData<Cinema>
}