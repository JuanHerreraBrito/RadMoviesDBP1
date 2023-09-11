package com.darooma.radmoviesdbp1.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.darooma.radmoviesdbp1.R
import com.darooma.radmoviesdbp1.data.db.model.RadMoviesEntity
import com.darooma.radmoviesdbp1.databinding.MovieElementBinding

class RadMovieAdapter(private val onMovieClick: (RadMoviesEntity) -> Unit): RecyclerView.Adapter<RadMovieAdapter.ViewHolder>() {

    private var movies: List<RadMoviesEntity> = emptyList()

    class ViewHolder(private val binding: MovieElementBinding): RecyclerView.ViewHolder(binding.root){
        val ivGenre= binding.imGenre
        //val carrito = binding.Carrito

        fun bind(movie: RadMoviesEntity, context: Context){
            /*binding.tvTitle.text = game.title
            binding.tvGenre.text = game.genre
            binding.tvDeveloper.text = game.developer*/

            binding.apply {
                tvTitle.text = movie.title
                tvDirector.text = movie.director
                tvProducer.text = movie.producer
                imGenre.setImageDrawable(ContextCompat.getDrawable( context, context.resources.getIdentifier( movie.genre  , "drawable", context.packageName)))//ponerLogica de Imagen
                imRating.setImageDrawable(ContextCompat.getDrawable( context, context.resources.getIdentifier( movie.rating  , "drawable", context.packageName)))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MovieElementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = movies.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(movies[position], holder.ivGenre.context)

        holder.itemView.setOnClickListener {
            //Aquí va el click del elemento
            //Deberia recibirlo la clase
            onMovieClick(movies[position])
        }

        holder.ivGenre.setOnClickListener {

        }

/*        holder.carrito.setOnClickListener {

        }*/
    }

    fun updateList(list: List<RadMoviesEntity>){
        movies = list
        notifyDataSetChanged()
    }
}


