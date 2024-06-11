package com.example.projekt

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class MoneyFragment : Fragment() {

    private lateinit var queue: RequestQueue
    private lateinit var topUpEditText: EditText
    private lateinit var topUpButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        queue = Volley.newRequestQueue(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_money, container, false)
        topUpEditText = view.findViewById(R.id.topUpEditText)
        topUpButton = view.findViewById(R.id.topUpButton)

        topUpButton.setOnClickListener {
            val amount = topUpEditText.text.toString().toDoubleOrNull()
            if (amount != null && amount > 0) {
                initiatePayPalPayment(amount)
            } else {
                // Show error message to the user
            }
        }

        return view
    }

    private fun initiatePayPalPayment(amount: Double) {
        val url = "http://10.0.2.2:5000/paypal/topup"
        val jsonBody = JSONObject().apply {
            put("amount", amount)
            put("user_id", UserSession.uid)
        }

        val request = JsonObjectRequest(
            Request.Method.POST, url, jsonBody,
            { response ->
                val paymentID = response.getString("paymentID")
                // Redirect to PayPal for payment
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.sandbox.paypal.com/checkoutnow?token=$paymentID"))
                startActivity(intent)
            },
            { error ->
                val errorMessage = error.message ?: "Unknown error occurred"
                Log.e("MoneyFragment", "Error initiating PayPal payment: $errorMessage")
            }
        )

        queue.add(request)
    }
}
