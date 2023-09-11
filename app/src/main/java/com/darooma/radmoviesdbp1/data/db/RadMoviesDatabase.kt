package com.darooma.radmoviesdbp1.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.darooma.radmoviesdbp1.data.db.model.RadMoviesEntity
import com.darooma.radmoviesdbp1.util.Constants

//room requiere clase abstracta

@Database(
    entities = [RadMoviesEntity::class],
    version = 1,  //versión de la BD. Importante para las migraciones
    exportSchema = true //por defecto es true
)
abstract class RadMoviesDatabase: RoomDatabase() { //Tiene que se abstracta

    //Aquí va el DAO
    abstract fun radMoviesDao(): RadMoviesDao

    //Sin inyección de dependencias, metemos la creación de la bd con un singleton aquí
    companion object{
        @Volatile //lo que se escriba en este campo, será inmediatamente visible a otros hilos
        private var INSTANCE: RadMoviesDatabase? = null

        fun getDatabase(context: Context): RadMoviesDatabase{
            return INSTANCE?: synchronized(this){
                //Si la instancia no es nula, entonces se regresa
                // si es nula, entonces se crea la base de datos (patrón singleton)
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RadMoviesDatabase::class.java,
                    Constants.DATABASE_NAME
                ).fallbackToDestructiveMigration() //Permite a Room recrear las tablas de la BD si las migraciones no se encuentran
                    .build()

                INSTANCE = instance

                instance
            }
        }
    }

}
