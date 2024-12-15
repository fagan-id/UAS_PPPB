package com.example.myuas_pppb.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.myuas_pppb.R
import com.example.myuas_pppb.model.Favorite

class FavoriteAdapter(
    private val favoriteList: List<Favorite>,
    private val onItemClick: (Favorite) -> Unit)
    : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    // ViewHolder class to hold references to the views
    class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imgDish)
        val nameTextView: TextView = itemView.findViewById(R.id.tvDishName)
        val ratingTextView: TextView = itemView.findViewById(R.id.tvRating)
        val priceTextView: TextView = itemView.findViewById(R.id.tvPrice)
        val descriptionTextView: TextView = itemView.findViewById(R.id.tvDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteAdapter.FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_menu, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val favorite = favoriteList[position]

        // Bind data
        holder.nameTextView.text = favorite.name
        holder.descriptionTextView.text = favorite.description
        holder.priceTextView.text = "Rp ${favorite.price}"
        holder.ratingTextView.text = favorite.rating.toString()

        // Load image using Glide
        Glide.with(holder.itemView.context)
            .load(favorite.imageResId)
            .placeholder(R.drawable.backland_leafy2)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(holder.imageView)

        // Click listener
        holder.itemView.setOnClickListener {
            onItemClick(favorite)
        }
    }

    override fun getItemCount(): Int = favoriteList.size
}
