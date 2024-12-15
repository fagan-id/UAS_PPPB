package com.example.myuas_pppb

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.myuas_pppb.database.CartDao
import com.example.myuas_pppb.database.CartDatabase
import com.example.myuas_pppb.database.FavoriteDao
import com.example.myuas_pppb.database.FavoriteDatabase
import com.example.myuas_pppb.databinding.FragmentDetailBinding
import com.example.myuas_pppb.model.Cart
import com.example.myuas_pppb.model.Favorite
import com.example.myuas_pppb.model.Menu
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class DetailFragment : Fragment() {
    // binding
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var executorService: ExecutorService
    private lateinit var favoriteDao: FavoriteDao
    private lateinit var cartDao: CartDao

    private var currentQuantity = 1 // Default quantity


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // inisiasi Database
        executorService = Executors.newSingleThreadExecutor()
        val db = FavoriteDatabase.getDatabase(requireContext())
        favoriteDao = db!!.favoriteDao()

        val db1 = CartDatabase.getDatabase(requireContext())
        cartDao = db1!!.cartDao()

        // Get Data Passed From the HomeFragment
        arguments?.let { bundle ->
            val id = bundle.getInt("id")
            val name = bundle.getString("name")
            val type = bundle.getString("type")
            val rating = bundle.getDouble("rating")
            val description = bundle.getString("description")
            val price = bundle.getDouble("price")
            val imageResId = bundle.getString("imageResId")


            // Use the data (e.g., update UI)
            with(binding) {
                tvName.text = name
                tvDescription.text = description
                tvPrice.text = "Rp.$price"
                ratingBar.rating = rating.toFloat()

                Glide.with(requireContext())
                    .load(imageResId) // Image URL or path
                    .placeholder(R.drawable.backland_leafy2) // Placeholder image
                    .error(R.drawable.backland_leafy2) // Error image if loading fails
                    .into(ivCoffeeImage)


                btnBack.setOnClickListener {
                    findNavController().navigateUp()
                }
                btnIncrease.setOnClickListener {
                    currentQuantity++
                    tvQuantity.text = currentQuantity.toString()
                }
                btnDecrease.setOnClickListener {
                    if (currentQuantity > 1) {
                        currentQuantity--
                        tvQuantity.text = currentQuantity.toString()
                    }
                }

                btnFavorite.setOnClickListener {
                    CoroutineScope(Dispatchers.IO).launch {
                        val isAlreadyFavorite = favoriteDao.isFavorite(name ?: "") > 0
                        withContext(Dispatchers.Main) {
                            if (isAlreadyFavorite) {
                                // Remove from favorites
                                CoroutineScope(Dispatchers.IO).launch {
                                    favoriteDao.deleteFavorite(name ?: "")
                                    withContext(Dispatchers.Main) {
                                        Toast.makeText(
                                            requireContext(),
                                            "$name removed from favorites",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            } else {
                                // Add to favorites
                                insertToFavorite(
                                    Favorite(
                                        name = name ?: " ",
                                        type = type ?: " ",
                                        rating = rating,
                                        description = description ?: " ",
                                        price = price,
                                        imageResId = imageResId ?: " "
                                    )
                                )
                                Toast.makeText(
                                    requireContext(),
                                    "$name added to favorites",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
                btnAddToCart.setOnClickListener {
                    val cartItem = Cart(
                        name = name ?: "Unnamed",
                        type = type ?: "Unknown",
                        rating = rating,
                        description = description ?: "",
                        price = price,
                        imageResId = imageResId ?: "",
                        qty = currentQuantity
                    )
                    addToCart(cartItem)
                }
                }
            }
        }

    private fun insertToFavorite(favorite: Favorite) {

        CoroutineScope(Dispatchers.IO).launch {
            try {
                favoriteDao.insertFavorite(favorite)
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "${favorite.name} added to favorites", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Error adding ${favorite.name} to favorites", Toast.LENGTH_SHORT).show()
                    Log.e("InsertFavorite", "Error inserting favorite: ${e.localizedMessage}", e)
                }
            }
        }
    }

    private fun addToCart(cart : Cart){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Check if the item already exists in the cart
                val existingCartItem = cartDao.getCartItem(cart.name)

                if (existingCartItem != null) {
                    // If exists, update the quantity
                    existingCartItem.qty += cart.qty
                    cartDao.insertCartItem(existingCartItem)
                } else {
                    // If not, insert a new item
                    cartDao.insertCartItem(cart)
                }
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "${cart.name} added to cart", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Error adding to cart", Toast.LENGTH_SHORT).show()
                    Log.e("CartError", e.message ?: "Error")
                }
            }
        }
    }

}