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
import java.security.MessageDigest

import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject


fun hash(password: String): String {
    val bytes = password.toByteArray()
    val md = MessageDigest.getInstance("SHA-256")
    val digest = md.digest(bytes)
    return digest.fold("") { str, it -> str + "%02x".format(it) }
}


class loginFragment : Fragment() {

    private lateinit var queue: RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        queue = Volley.newRequestQueue(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        val loginButton = view.findViewById<Button>(R.id.login2Button)

        loginButton.setOnClickListener {
            val email = view.findViewById<EditText>(R.id.textView2).text.toString()
            val haslo = view.findViewById<EditText>(R.id.textView3).text.toString()
            val hashedPassword = hash(haslo)

            loginUser(email, hashedPassword)
        }
        return view
    }


    private fun loginUser(email: String, hashPass: String) {
        val url = "http://10.0.2.2:5000/login"
        val jsonBody = JSONObject().apply {
            put("email", email)
            put("haslo", hashPass)
            println(hashPass)
        }

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url, jsonBody,
            { response ->
                val message = response.optString("message", "No message")
                if (message == "Login successful!") {
                    UserSession.email = email
                    findNavController().navigate(R.id.action_loginFragment_to_afterLoginFragment)
                    context?.let {
                        Toast.makeText(it, "Poprawnie zalogowano!", Toast.LENGTH_SHORT).show()}
                } else {
                    context?.let {
                        Toast.makeText(it, "Zły email lub hasło!", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            { error ->
                val errorMessage = error.message ?: "Unknown error occurred"
                Log.e("loginFragment", "Login error: $errorMessage")
                context?.let {
                    Toast.makeText(it, "Error occurred: $errorMessage", Toast.LENGTH_SHORT).show()
                }
            }
        )

        queue.add(jsonObjectRequest)
    }
}
