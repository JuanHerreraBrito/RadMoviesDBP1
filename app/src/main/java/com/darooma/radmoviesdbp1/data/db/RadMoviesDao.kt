package com.darooma.radmoviesdbp1.data.db

import androidx.room.*
import com.darooma.radmoviesdbp1.data.db.model.RadMoviesEntity
import com.darooma.radmoviesdbp1.util.Constants

//darta access object
@Dao
interface RadMoviesDao {

    //Create
    @Insert
    suspend fun insertRadMovie(movie: RadMoviesEntity)//funciones suspendidas se ejecutan en corrutinas, y se suspenda si el sisyema lo requiere


    @Insert
    suspend fun insertRadMovies(movies: List<RadMoviesEntity>)
    //Read
    @Query("SELECT * FROM ${Constants.DATABASE_MOVIE_TABLE}")
    suspend fun getAllRadMovies():List<RadMoviesEntity>

    //Update
    @Update
    suspend fun updateRadMovie(movie: RadMoviesEntity)

    //Delete
    @Delete
    suspend fun deleteRadMovie(movie: RadMoviesEntity)

}