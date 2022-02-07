package com.example.films.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.films.databinding.ItemsFilmsBinding
import com.example.films.domain.model.entities.FilmEntities

class FilmsAdapter: PagingDataAdapter<FilmEntities, FilmsAdapter.FilmViewHolder>(
    FILMS_COMPARATOR) {

    inner class FilmViewHolder(var binding: ItemsFilmsBinding) : RecyclerView.ViewHolder(binding.root)

    companion object{
        private val FILMS_COMPARATOR = object : DiffUtil.ItemCallback<FilmEntities>(){
            override fun areItemsTheSame(oldItem: FilmEntities, newItem: FilmEntities) =
                oldItem.imdbID == newItem.imdbID
            override fun areContentsTheSame(
                oldItem: FilmEntities,
                newItem: FilmEntities
            ) = oldItem.imdbID == newItem.imdbID

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmViewHolder {
        val binding = ItemsFilmsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FilmViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {
        val films = getItem(position)
        if (films != null) {
            if(films.path.isNotEmpty()) {
                holder.itemView.apply {
                    Glide.with(this).load(films.path).into(holder.binding.ivFilms)
                }
            } else {
                holder.itemView.apply {
                    Glide.with(this).load(films.Poster).into(holder.binding.ivFilms)
                }
            }
        }

        holder.binding.tvTitle.text = films?.Title
        holder.binding.tvYear.text = films?.Year
        holder.binding.tvMuv.text = films?.Type
        val movie = holder.binding.tvMuv.text

        if (movie != "movie") {
            holder.binding.tvMuv.visibility = View.VISIBLE
            holder.binding.tvMuv.text = films?.Type
        }

        holder.binding.constraintItems.setOnClickListener {
            onItemClickListner.let {
                if (films != null) {
                    it(films)
                }
            }
        }

    }

    private var onItemClickListner: (FilmEntities)->Unit = { films: FilmEntities -> Unit }

    fun setOnItemClickListner(listner: (FilmEntities) ->Unit) {
        onItemClickListner = listner
    }
}