package com.darooma.radmoviesdbp1.application

import android.app.Application
import com.darooma.radmoviesdbp1.data.RadMoviesRepository
import com.darooma.radmoviesdbp1.data.db.RadMoviesDatabase

//Para poderle meter inyeccion de dependencias

class RadMoviesDBApp(): Application() {
    private val  database by lazy{
        RadMoviesDatabase.getDatabase(this@RadMoviesDBApp)
    }

    val repository by lazy{
        RadMoviesRepository(database.radMoviesDao())
    }
}