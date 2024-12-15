package com.example.myuas_pppb

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myuas_pppb.adapter.CarouselAdapter
import com.example.myuas_pppb.adapter.CarouselItem
import com.example.myuas_pppb.adapter.MenuAdapter
import com.example.myuas_pppb.databinding.FragmentHomeBinding
import com.example.myuas_pppb.model.Menu
import com.example.myuas_pppb.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class HomeFragment : Fragment() {
    private val binding by lazy{
        FragmentHomeBinding.inflate(layoutInflater)
    }


    // Pemanggilan Instansi
    private lateinit var prefManager: PrefManager
    private lateinit var menuAdapter: MenuAdapter
    private val menuList = mutableListOf<Menu>()
    private val filteredMenulist = mutableListOf<Menu>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        prefManager = PrefManager.getInstance(requireContext())

        // Pemanggilan Recycler View
        fetchMenuData(10)
        setupCarousel()
        setupMenu()
        setupCatalog()

        with(binding){
            val name = prefManager.getUsername().toString()
            tvGreeting.text = "Hai, $name!"
        }

    }

    fun setupCarousel(){
        val carouselItems = listOf(
            CarouselItem(R.drawable.image1, "Item 1"),
            CarouselItem(R.drawable.image2, "Item 2"),
            CarouselItem(R.drawable.image3, "Item 3")
        )

        val adapter = CarouselAdapter(carouselItems)
        binding.carouselViewPager.adapter = adapter

        // Optional: Add page transformation for carousel effect
        binding.carouselViewPager.setPageTransformer { page, position ->
            page.scaleY = 1 - Math.abs(position) * 0.25f
        }
    }

    fun setupMenu() {
        menuAdapter = MenuAdapter(menuList) { menu ->
            val bundle = Bundle().apply {
                putInt("id", menu.id)
                putString("name", menu.name)
                putString("type", menu.type)
                putDouble("rating", menu.rating)
                putString("description", menu.description)
                putDouble("price", menu.price)
                putString("imageResId", menu.imageResId)
            }
            findNavController().navigate(R.id.action_homeFragment_to_detailFragment2, bundle)
        }
        // Bind Menu
        binding.rvPopular.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = menuAdapter
        }
    }

    fun setupCatalog(){
        menuAdapter = MenuAdapter(filteredMenulist) { menu ->
            val bundle = Bundle().apply {
                putInt("id", menu.id)
                putString("name", menu.name)
                putString("type", menu.type)
                putDouble("rating", menu.rating)
                putString("description", menu.description)
                putDouble("price", menu.price)
                putString("imageResId", menu.imageResId)
            }
            findNavController().navigate(R.id.action_homeFragment_to_detailFragment2, bundle)
        }

        binding.rvOffers.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = menuAdapter
        }

        filterMenu("All")

        // Set click listeners for each chip
        binding.chipAll.setOnClickListener {
            filterMenu("All")
        }

        binding.chipFood.setOnClickListener {
            filterMenu("foods")
        }

        binding.chipDrinks.setOnClickListener {
            filterMenu("beverages")
        }

        binding.chipDessert.setOnClickListener {
            filterMenu("dessert")
        }

        binding.chipCoffee.setOnClickListener {
            filterMenu("coffee")
        }

        // Initially show all items
        filterMenu("All")
    }

    private fun filterMenu(type: String) {
        filteredMenulist.clear()

        if (type == "All") {
            filteredMenulist.addAll(menuList)
        } else {
            filteredMenulist.addAll(menuList.filter { it.type.lowercase() == type.lowercase() })
        }

        // Show or hide the "No Item In This Catalog" TextView
        if (filteredMenulist.isEmpty()) {
            binding.tvNoItems.visibility = View.VISIBLE
        } else {
            binding.tvNoItems.visibility = View.GONE
        }
        menuAdapter.notifyDataSetChanged()
    }

    private fun fetchMenuData(count: Int) {
        val client = ApiClient.getInstance()

        client.getAllMenu().enqueue(object : Callback<List<Menu>> {
            override fun onResponse(call: Call<List<Menu>>, response: Response<List<Menu>>) {
                if (response.isSuccessful && response.body() != null) {
                    val menus = response.body()
                    menus?.let {
                        menuList.clear() // Clear previous data
                        menuList.addAll(it) // Add all menu items to the list
                        menuAdapter.notifyDataSetChanged() // Notify adapter about data change
                        filterMenu("All") // Update filter after data load
                        Log.d("Menu Items", menuList.toString())

                    }
                } else {
                    Log.e("HomeFragment", "Response failed: ${response.errorBody()?.string()}")
                    Toast.makeText(requireContext(), "Failed to load menus", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Menu>>, t: Throwable) {
                Log.e("HomeFragment", "Network request failed: ${t.message}")
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


}