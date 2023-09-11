package com.darooma.radmoviesdbp1.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.darooma.radmoviesdbp1.util.Constants

//se entiende que se trajo de la biblioteca ROOM
@Entity(tableName = Constants.DATABASE_MOVIE_TABLE)
data class RadMoviesEntity(
    //ANotaciones de room
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_movie")
    val id: Long = 0,
    @ColumnInfo(name = "title")
    var title: String,
    @ColumnInfo(name = "producer")
    var producer: String,
    @ColumnInfo(name = "director")
    var director: String,
    @ColumnInfo(name = "genre")
    var genre: String,
    @ColumnInfo(name = "rating")
    var rating: String,


)
