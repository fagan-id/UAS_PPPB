package com.example.myuas_pppb.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.myuas_pppb.model.Menu
import com.example.myuas_pppb.R


class MenuAdapter(
    private val menuList: List<Menu>,
    private val onItemClick: (Menu) -> Unit
) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    // ViewHolder class to hold references to the views
    class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imgDish)
        val nameTextView: TextView = itemView.findViewById(R.id.tvDishName)
        val ratingTextView: TextView = itemView.findViewById(R.id.tvRating)
        val priceTextView: TextView = itemView.findViewById(R.id.tvPrice) // Reuse for price
        val descriptionTextView: TextView = itemView.findViewById(R.id.tvDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        // Inflate the layout for each menu item
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_menu, parent, false)
        return MenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val menu = menuList[position]

        // Load image using Glide
        Glide.with(holder.itemView.context)
            .load(menu.imageResId) // Assuming API returns a URL for the image
            .placeholder(R.drawable.backland_leafy2) // Add a placeholder image
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(holder.imageView)

        // Bind other data
        holder.nameTextView.text = menu.name
        holder.descriptionTextView.text = menu.description
        holder.priceTextView.text = "Rp ${menu.price}" // Format price
        holder.ratingTextView.text = menu.rating.toString()

        // Click Listener to see detail
        holder.itemView.setOnClickListener {
            // Handle item click here
            onItemClick(menu)
        }
    }

    override fun getItemCount(): Int = menuList.size
}