package com.example.projekt

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import org.json.JSONArray
class AccountFragment : Fragment() {

    private lateinit var queue: RequestQueue
    private lateinit var nameTextView: TextView
    private lateinit var surnameTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var accountStatusTextView: TextView
    private lateinit var rentalsContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        queue = Volley.newRequestQueue(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_account, container, false)
        nameTextView = view.findViewById(R.id.nameTextView)
        surnameTextView = view.findViewById(R.id.surnameTextView)
        emailTextView = view.findViewById(R.id.emailTextView)
        accountStatusTextView = view.findViewById(R.id.accountStatusTextView)
        rentalsContainer = view.findViewById(R.id.rentalsContainer)

        val email = UserSession.email
        if (email != null) {
            fetchUserData(email)
        }

        return view
    }

    private fun fetchUserData(email: String) {
        val url = "http://10.0.2.2:5000/users/$email"

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                displayUserData(response)
                fetchUserRentals(response.optInt("id"))
            },
            { error ->
                val errorMessage = error.message ?: "Unknown error occurred"
                Log.e("AccountFragment", "Error fetching user data: $errorMessage")
            }
        )

        queue.add(jsonObjectRequest)
    }

    private fun displayUserData(response: JSONObject) {
        val imie = response.optString("imie", "Unknown")
        val nazwisko = response.optString("nazwisko", "Unknown")
        val email = response.optString("email", "Unknown")
        val stanKonta = response.optInt("stan_konta", 0)

        nameTextView.text = "Imię: $imie"
        surnameTextView.text = "Nazwisko: $nazwisko"
        emailTextView.text = "Email: $email"
        accountStatusTextView.text = "Stan konta: $stanKonta"
    }

    private fun fetchUserRentals(userId: Int) {
        val url = "http://10.0.2.2:5000/rentals?userId=$userId"

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                displayUserRentals(response)
            },
            { error ->
                val errorMessage = error.message ?: "Unknown error occurred"
                Log.e("AccountFragment", "Error fetching user rentals: $errorMessage")
            }
        )

        queue.add(jsonArrayRequest)
    }

    private fun displayUserRentals(response: JSONArray) {
        for (i in 0 until response.length()) {
            val rental = response.getJSONObject(i)
            val rentalView = TextView(requireContext()).apply {
                text = "Książka: ${rental.optString("tytul")} | Data wypożyczenia: ${rental.optString("data_wypozyczenia")} | Data zwrotu:${rental.optString("data_zwrotu")}"
                textSize = 18f
            }
            rentalsContainer.addView(rentalView)
        }
    }
}