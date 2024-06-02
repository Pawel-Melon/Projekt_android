package com.example.projekt

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class registerFragment : Fragment() {

    private lateinit var queue: RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        queue = Volley.newRequestQueue(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_register, container, false)
        val registerButton = view.findViewById<Button>(R.id.register2Button)

        registerButton.setOnClickListener {
            val email = view.findViewById<EditText>(R.id.textView4).text.toString()
            val haslo = view.findViewById<EditText>(R.id.editText).text.toString()
            val imie = view.findViewById<EditText>(R.id.editText2).text.toString()
            val nazwisko = view.findViewById<EditText>(R.id.editText3).text.toString()
            registerUser(imie, nazwisko, email, haslo)
        }

        return view
    }

    private fun registerUser(imie: String, nazwisko: String, email: String, haslo: String) {
        val url = "http://10.0.2.2:5000/register"
        val jsonBody = JSONObject().apply {
            put("imie", imie)
            put("nazwisko", nazwisko)
            put("email", email)
            put("haslo", haslo)
        }

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url, jsonBody,
            { response ->
                val message = response.getString("message")
                if (message == "User created!") {
                    Toast.makeText(context, "Poprawnie utworzono użytkownika!", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                } else {
                    Toast.makeText(context, "Użytkownik już istnieje!", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                Log.e("registerFragment", "Registration error: ${error.message}")
                Toast.makeText(context, "Error occurred: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        )

        queue.add(jsonObjectRequest)
    }
}