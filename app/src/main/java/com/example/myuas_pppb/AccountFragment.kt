package com.example.myuas_pppb

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myuas_pppb.databinding.FragmentAccountBinding

class AccountFragment : Fragment() {
    private val binding by lazy {
        FragmentAccountBinding.inflate(layoutInflater)
    }

    // SharedPreferences helper instance
    private lateinit var prefManager: PrefManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize SharedPreferences manager
        prefManager = PrefManager.getInstance(requireContext())

        // Bind username and email from SharedPreferences
        bindUserDetails()

        // Set logout button click listener
        binding.btnLogout.setOnClickListener {
            logoutUser()
        }
    }

    private fun bindUserDetails() {
        val username = prefManager.getUsername()
        val email = prefManager.getEmail()

        binding.tvUsername.text = username ?: "Username not found"
        binding.tvEmail.text = email ?: "Email not found"
    }

    private fun logoutUser() {
        // Clear user data in SharedPreferences
        prefManager.clearUser()
        // Redirect to login screen
        navigateToLogin()
    }

    private fun navigateToLogin() {
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }
}
