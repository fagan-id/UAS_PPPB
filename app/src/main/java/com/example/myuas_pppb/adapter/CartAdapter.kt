package com.example.myapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myuas_pppb.R
import com.example.myuas_pppb.model.Cart

class CartAdapter(
    private val context: Context,
    private val cartItems: MutableList<Cart>, // List of cart items
    private val onRemoveItem: (Cart) -> Unit, // Callback for removing an item
    private val onQuantityChanged: (Cart, Int) -> Unit // Callback for changing quantity
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    // ViewHolder for individual cart items
    inner class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemImage: ImageView = view.findViewById(R.id.itemImage)
        val itemName: TextView = view.findViewById(R.id.itemName)
        val itemPrice: TextView = view.findViewById(R.id.itemPrice)
        val itemQuantity: EditText = view.findViewById(R.id.itemQuantity)
        val removeButton: ImageButton = view.findViewById(R.id.itemRemoveButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cartItem = cartItems[position]

        // Bind the data to the views
        holder.itemName.text = cartItem.name
        holder.itemPrice.text = String.format("$%.2f", cartItem.price)
        holder.itemQuantity.setText(cartItem.qty.toString())

        // Load the image using Glide
        Glide.with(context)
            .load(cartItem.imageResId)
            .placeholder(R.drawable.backland_leafy2)
            .into(holder.itemImage)

        // Handle remove button click
        holder.removeButton.setOnClickListener {
            onRemoveItem(cartItem)
            cartItems.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, cartItems.size)
        }

        // Handle quantity change
        holder.itemQuantity.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val newQuantity = holder.itemQuantity.text.toString().toIntOrNull() ?: 1
                onQuantityChanged(cartItem, newQuantity)
                cartItem.qty = newQuantity
            }
        }
    }

    override fun getItemCount(): Int = cartItems.size
}
