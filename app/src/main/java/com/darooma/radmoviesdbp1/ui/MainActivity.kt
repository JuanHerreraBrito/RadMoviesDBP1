package com.darooma.radmoviesdbp1.ui

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.darooma.radmoviesdbp1.R
import com.darooma.radmoviesdbp1 .application.RadMoviesDBApp
import com.darooma.radmoviesdbp1.data.RadMoviesRepository
import com.darooma.radmoviesdbp1.data.db.model.RadMoviesEntity
import com.darooma.radmoviesdbp1.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var radMovies: List<RadMoviesEntity> = emptyList()
    private lateinit var repository: RadMoviesRepository

    private lateinit var radMovieAdapter: RadMovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repository = (application as RadMoviesDBApp).repository

        radMovieAdapter = RadMovieAdapter(){ movie ->
            radMovieClicked(movie)
        }

        //para ser mas especificos el arroba
        //binding.rvGames.layoutManager = LinearLayoutManager(this@MainActivity)
        //binding.rvGames.adapter = gameAdapter

        binding.rvGames.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = radMovieAdapter
        }

        updateUI()

    }

    private fun updateUI(){
        lifecycleScope.launch {
            radMovies = repository.getAllRadMovies()

            if(radMovies.isNotEmpty()){
                //Hay por lo menos un registro
                binding.tvSinRegistros.visibility = View.INVISIBLE
            }else{
                //No hay registros
                binding.tvSinRegistros.visibility = View.VISIBLE
            }
            radMovieAdapter.updateList(radMovies)

        }
    }

    fun click(view: View){
        val dialog = RadMovieDialog(updateUI = {
            updateUI()
        }, message = {text ->
            message(text)

        })
        dialog.show(supportFragmentManager, "dialog")
    }
    private fun radMovieClicked(movie: RadMoviesEntity){
        //Toast.makeText(this, "cliked en el juego ${game.title}", Toast.LENGTH_SHORT).show()
        val dialog = RadMovieDialog(newRadMovie = false, radMovie = movie, updateUI = {
            updateUI()
        }, message = {text ->
            message(text)

        })
        dialog.show(supportFragmentManager, "dialog")
    }

    private fun message(text: String){
        Snackbar.make(binding.cl, text, Snackbar.LENGTH_SHORT)
            .setTextColor(ContextCompat.getColor(this, R.color.white) )
            .setBackgroundTint(ContextCompat.getColor(this, R.color.color_snack) )
            .show()
    }
}
