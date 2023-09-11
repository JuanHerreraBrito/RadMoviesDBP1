package com.darooma.radmoviesdbp1.data


import com.darooma.radmoviesdbp1.data.db.RadMoviesDao
import com.darooma.radmoviesdbp1.data.db.model.RadMoviesEntity


//En este se definen cuestioens de datos online o del back
//
class RadMoviesRepository(private val radMoviesDao: RadMoviesDao) {
//Arquitectura limpia, el interactor se tiene antes del repositorio

    suspend fun insertRadMovie(movie: RadMoviesEntity){
        radMoviesDao.insertRadMovie(movie)

    }

    /*
    suspend fun insertGame(title: String, genre: String, developer: String){
        gameDao.insertGame(GameEntity(title = title, genre = genre, developer = developer))
    }

     */

    suspend fun getAllRadMovies(): List<RadMoviesEntity> = radMoviesDao.getAllRadMovies()

    suspend fun updateRadMovie(movie: RadMoviesEntity){
        radMoviesDao.updateRadMovie(movie)
    }

    suspend fun deleteRadMovie(movie: RadMoviesEntity){
        radMoviesDao.deleteRadMovie(movie)
    }
}