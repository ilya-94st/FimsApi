package com.example.fims.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fims.databinding.ItemsFilmsBinding
import com.example.fims.domain.model.Films

class FilmsAdapter: RecyclerView.Adapter<FilmsAdapter.FilmViewHolder>() {

    inner class FilmViewHolder(val binding: ItemsFilmsBinding) : RecyclerView.ViewHolder(binding.root)

    val diffCallback = object : DiffUtil.ItemCallback<Films>() {
        override fun areItemsTheSame(oldItem: Films, newItem: Films): Boolean {
            return oldItem.imdbID == newItem.imdbID
        }

        override fun areContentsTheSame(oldItem: Films, newItem: Films): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

   private val differ = AsyncListDiffer(this, diffCallback)

    fun submitList(list: List<Films>) = differ.submitList(list)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmViewHolder {
        val binding = ItemsFilmsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FilmViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {
        val films = differ.currentList[position]

        holder.itemView.apply {
            Glide.with(this).load(films.Poster).into(holder.binding.ivFilms)
        }

        holder.binding.tvTitle.text = films.Title
        holder.binding.tvYear.text = films.Year
        holder.binding.tvMuv.text = films.Type
        val movie = holder.binding.tvMuv.text

        if (movie != "movie") {
            holder.binding.tvMuv.visibility = View.VISIBLE
            holder.binding.tvMuv.text = films.Type
        }

        holder.binding.constraintItems.setOnClickListener {
            onItemClickListner.let {
                it(films)
            }
        }

    }

    private var onItemClickListner: (Films)->Unit = {films: Films -> Unit }

    fun setOnItemClickListner(listner: (Films) ->Unit) {
        onItemClickListner = listner
    }
}