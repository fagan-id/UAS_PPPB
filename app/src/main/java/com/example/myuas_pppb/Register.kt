package com.example.myuas_pppb

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import com.example.myuas_pppb.databinding.FragmentRegisterBinding

/**
 * A simple [Fragment] subclass.
 * Use the [Register.newInstance] factory method to
 * create an instance of this fragment.
 */
class Register : Fragment() {
    private val binding by lazy{
        FragmentRegisterBinding.inflate(layoutInflater)
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

        with(binding){
            btnToLogin.setOnClickListener {
                val action = RegisterDirections.actionRegisterToLogin()
                findNavController().navigate(action)
            }
            btnRegister.setOnClickListener {
                registerUser()
            }
        }
    }

    private fun registerUser(){
        val username = binding.inputUsername.text.toString()
        val password = binding.inputPassword.text.toString()
        val email = binding.inputEmail.text.toString()


        // Validate inputs
        if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Optional: Check if a user is already registered
        if (prefManager.getUsername() != null) {
            Toast.makeText(requireContext(), "User already registered", Toast.LENGTH_SHORT).show()
            return
        }

        // Simpan data pengguna ke PrefManager
        prefManager.saveUser(username, password, email)
        Toast.makeText(requireContext(), "Registration successful", Toast.LENGTH_SHORT).show()

        // Navigasi kembali ke halaman login
        val action = RegisterDirections.actionRegisterToLogin()
        findNavController().navigate(action)
    }
}