package com.example.projekt

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class HomeFragment : Fragment() {

    private lateinit var queue: RequestQueue
    private lateinit var welcomeTextView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        queue = Volley.newRequestQueue(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val button = view.findViewById<Button>(R.id.mapButton)
        val accountButton = view.findViewById<Button>(R.id.accountButton)
        val catalogbutton = view.findViewById<Button>(R.id.catalogueButton)
        val moneyButton = view.findViewById<Button>(R.id.moneyButton)
        button.setOnClickListener {
            findNavController().navigate(R.id.action_afterLoginFragment_to_mapFragment)
        }
        accountButton.setOnClickListener {
            findNavController().navigate(R.id.action_afterLoginFragment_to_accountFragment)
        }
        catalogbutton.setOnClickListener {
            findNavController().navigate(R.id.action_afterLoginFragment_to_catalogFragment)
        }
        moneyButton.setOnClickListener {
            findNavController().navigate(R.id.action_afterLoginFragment_to_moneyFragment)
        }
        welcomeTextView = view.findViewById(R.id.textView)
        val email = UserSession.email
        if (email != null) {
            fetchUserData(email)
        } else {
            welcomeTextView.text = "No email found!"
        }

        return view
    }



    private fun fetchUserData(email: String) {
        val url = "http://10.0.2.2:5000/users/$email"

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                val imie = response.optString("imie", "Unknown")
                val u_id = response.optInt("id_uzytkownika", 7)
                UserSession.uid = u_id
                welcomeTextView.text = "Witaj, $imie!"
            },
            { error ->
                val errorMessage = error.message ?: "Unknown error occurred"
                Log.e("afterLoginFragment", "Error fetching user data: $errorMessage")
                welcomeTextView.text = "Error occurred: $errorMessage"
            }

        )

        queue.add(jsonObjectRequest)

    }

}