package com.example.projekt

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController


class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val loginbutton = view.findViewById<Button>(R.id.loginButton)
        val registerbutton = view.findViewById<Button>(R.id.registerButton)

        loginbutton.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
        }

        registerbutton.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_registerFragment)
        }
        return view


    }


}