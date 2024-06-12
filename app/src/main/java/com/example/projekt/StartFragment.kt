package com.example.projekt

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject


class StartFragment : Fragment() {

    private lateinit var welcomeTextView: TextView
    private lateinit var loginButton: Button
    private lateinit var registerButton: Button
    private lateinit var queue: RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        queue = Volley.newRequestQueue(requireContext())
    }

    @SuppressLint("CutPasteId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_start, container, false)

        welcomeTextView = view.findViewById(R.id.textView)
        loginButton = view.findViewById(R.id.loginButton)
        registerButton = view.findViewById(R.id.registerButton)

        loginButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
        }

        registerButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_registerFragment)
        }

        val welcometext = view.findViewById<TextView>(R.id.textView)
        val url = "http://192.168.1.17:5000/users/test"


        //fetchUserData()
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                try {
                    val jsonObject = JSONObject(response)
                    val imie = jsonObject.getString("imie")
                    println(imie)
                    if(imie=="1"){welcometext.text = "[API online]"}

                } catch (e: Exception) {
                    Log.e("homefragment", "Error parsing JSON: ${e.message}")
                }
            },
            { error ->
                Log.e("homefragment", "Request error: ${error.message}")
            })
        queue.add(stringRequest)
        return view
    }



}