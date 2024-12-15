package com.example.myuas_pppb

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp.adapters.CartAdapter
import com.example.myuas_pppb.database.CartDao
import com.example.myuas_pppb.database.CartDatabase
import com.example.myuas_pppb.databinding.FragmentCartBinding
import com.example.myuas_pppb.model.Cart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CartFragment : Fragment() {

    private val binding by lazy {
        FragmentCartBinding.inflate(layoutInflater)
    }
    private lateinit var cartAdapter: CartAdapter
    private lateinit var cartDao: CartDao

    private val cartItems = mutableListOf<Cart>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Database
        val db = CartDatabase.getDatabase(requireContext())
        cartDao = db!!.cartDao()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateTotalPrice()
        setupCartRecyclerView()
        getAllCartItems()

        with(binding) {

            cartCheckoutButton.setOnClickListener {
                Toast.makeText(requireContext(), "Checkout Successful,", Toast.LENGTH_SHORT).show()

                // Clear cart items and update the RecyclerView
                lifecycleScope.launch(Dispatchers.IO) {
                    cartDao.clearAllCartItems()
                    requireActivity().runOnUiThread {
                        cartItems.clear()
                        cartAdapter.notifyDataSetChanged()
                        updateTotalPrice()
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        updateTotalPrice()
    }

    fun getAllCartItems() {
        cartDao.getAllCartItems().observe(viewLifecycleOwner) { cartItems ->
            this.cartItems.clear()
            this.cartItems.addAll(cartItems)
            cartAdapter.notifyDataSetChanged()
        }
    }

    private fun setupCartRecyclerView() {
        cartAdapter = CartAdapter(
            requireContext(),
            cartItems,
            onRemoveItem = { cartItem ->
                if (cartItems.isNotEmpty()) {
                    Toast.makeText(requireContext(), "${cartItem.name} removed", Toast.LENGTH_SHORT).show()
                    lifecycleScope.launch(Dispatchers.IO) {
                        cartDao.deleteCartItem(cartItem)  // Safe deletion
                        requireActivity().runOnUiThread {
                            cartItems.remove(cartItem)
                            updateTotalPrice()
                        }
                    }
                } else {
                    Toast.makeText(requireContext(), "No items to remove", Toast.LENGTH_SHORT).show()
                }
            },
            onQuantityChanged = { cartItem, newQuantity ->
                lifecycleScope.launch(Dispatchers.IO) {
                    cartDao.updateCartItemQuantity(cartItem.id, newQuantity)  // Safe update
                    requireActivity().runOnUiThread {
                        cartItem.qty = newQuantity
                        updateTotalPrice()
                    }
                }
            }
        )
        binding.cartRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = cartAdapter
        }
    }


    private fun updateTotalPrice() {
        val total = cartItems.sumOf { it.price * it.qty }
        binding.cartTotalPrice.text = String.format("Total: Rp.%.2f", total)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Clear binding references
        binding.cartRecyclerView.adapter = null
    }
}
