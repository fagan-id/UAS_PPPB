package com.example.myuas_pppb

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import com.example.myuas_pppb.databinding.FragmentLoginBinding

/**
 * A simple [Fragment] subclass.
 * Use the [Login.newInstance] factory method to
 * create an instance of this fragment.
 */
class Login : Fragment() {

    private val binding by lazy{
        FragmentLoginBinding.inflate(layoutInflater)
    }

    // membuat variabel untuk menyimpan PrefManager
    private lateinit var prefManager: PrefManager

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

        // Inisialisasi PrefManager
        prefManager = PrefManager.getInstance(requireContext())

        // Cek Apakah Sudah Login Sebelumnya
        checkLoginStatus()

        with(binding){
            btnToRegister.setOnClickListener{
                val action = LoginDirections.actionLoginToRegister3()
                findNavController().navigate(action)
            }
            btnLogin.setOnClickListener{
                loginUser()
            }
        }
    }

    private fun loginUser(){
        val username = binding.inputUsername.text.toString()
        val password = binding.inputPassword.text.toString()

        // Get Saved Pref
        val savedUsername = prefManager.getUsername()
        val savedPassword = prefManager.getPassword()

        // Validate inputs
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Check if the user is registered
        if (savedUsername == null || savedPassword == null) {
            Toast.makeText(requireContext(), "No registered user found. Please register first.", Toast.LENGTH_SHORT).show()
            return
        }

        if (username == savedUsername && password == savedPassword) {
            Toast.makeText(requireContext(), "Login Successful", Toast.LENGTH_SHORT).show()
            // Navigate to the main/home screen of your app
            findNavController().navigate(R.id.action_login_to_mainActivity)
        } else {
            Toast.makeText(requireContext(), "Invalid username or password", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkLoginStatus(){
        val username = prefManager.getUsername()
        val password = prefManager.getPassword()
        if (username != null && password != null){
            // Navigate to the main/home screen of your app
            findNavController().navigate(R.id.action_login_to_mainActivity)
        }
    }
}