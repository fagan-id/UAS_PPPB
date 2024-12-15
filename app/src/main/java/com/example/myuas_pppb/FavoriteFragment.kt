package com.example.myuas_pppb

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myuas_pppb.adapter.FavoriteAdapter
import com.example.myuas_pppb.database.FavoriteDao
import com.example.myuas_pppb.database.FavoriteDatabase
import com.example.myuas_pppb.databinding.FragmentFavoriteBinding
import com.example.myuas_pppb.model.Favorite


/**
 * A simple [Fragment] subclass.
 * Use the [FavoriteFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FavoriteFragment : Fragment() {
    private val binding by lazy{
        FragmentFavoriteBinding.inflate(layoutInflater)
    }
    // Pemanggilan Instansi
    private lateinit var favoriteAdapter : FavoriteAdapter
    private val favoriteList = mutableListOf<Favorite>()
    private lateinit var favoriteDao: FavoriteDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // inisiasi Database
        val db = FavoriteDatabase.getDatabase(requireContext())
        favoriteDao = db!!.favoriteDao()
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
        setupFavorite()
        getFavoriteList()
        Log.d("FavoriteFragment", "Favorite List Size: ${favoriteList.size}")
    }

    private fun loadDummyData() {
        favoriteList.apply {
            add(
                Favorite(
                    1,
                    "Dummy Coffee 1",
                    "Coffee",
                    4.5,
                    "Delicious coffee",
                    5.0,
                    "dummy_image_1"
                )
            )
            add(Favorite(2, "Dummy Tea 1", "Tea", 4.0, "Refreshing tea", 4.0, "dummy_image_2"))
            add(
                Favorite(
                    3,
                    "Dummy Smoothie 1",
                    "Smoothie",
                    4.8,
                    "Healthy smoothie",
                    4.5,
                    "dummy_image_3"
                )
            )
            add(
                Favorite(
                    4,
                    "Dummy Coffee 1",
                    "Coffee",
                    4.5,
                    "Delicious coffee",
                    5.0,
                    "dummy_image_1"
                )
            )
        }
    }

    private fun getFavoriteList() {
        favoriteDao.getAllFavorites().observe(viewLifecycleOwner) { favorites ->
            favoriteList.clear()
            favoriteList.addAll(favorites)
            favoriteAdapter.notifyDataSetChanged()
        }
    }
    private fun setupFavorite() {
            // Set On Click Logic
            favoriteAdapter = FavoriteAdapter(favoriteList) { favorite ->
                val bundle = Bundle().apply {
                    putInt("id", favorite.id)
                    putString("name", favorite.name)
                    putString("type", favorite.type)
                    putDouble("rating", favorite.rating)
                    putString("description", favorite.description)
                    putDouble("price", favorite.price)
                    putString("imageResId", favorite.imageResId)
                }
                findNavController().navigate(R.id.action_favoriteFragment_to_detailFragment, bundle)
            }
            binding.rvFavorites.apply {
                layoutManager = GridLayoutManager(requireContext(), 2)
                adapter = favoriteAdapter
            }
        }

    }