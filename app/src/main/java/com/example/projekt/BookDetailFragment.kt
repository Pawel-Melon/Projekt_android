package com.example.projekt

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.projekt.R
import com.example.projekt.UserSession
import org.json.JSONObject
import java.time.LocalDate

class BookDetailFragment : Fragment() {

    private lateinit var titleTextView: TextView
    private lateinit var authorTextView: TextView
    private lateinit var categoryTextView: TextView
    private lateinit var yearTextView: TextView
    private lateinit var copiesTextView: TextView
    private lateinit var rentButton: Button
    private lateinit var queue: RequestQueue
    private var bookId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        queue = Volley.newRequestQueue(requireContext())
        arguments?.let {
            bookId = it.getInt("bookId")
        } ?: run {
            // Obsługa przypadku, gdy brak argumentu bookId
            Log.e("BookDetailFragment", "No bookId argument provided!")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_book_detail, container, false)
        titleTextView = view.findViewById(R.id.titleTextView)
        authorTextView = view.findViewById(R.id.authorTextView)
        categoryTextView = view.findViewById(R.id.categoryTextView)
        yearTextView = view.findViewById(R.id.yearTextView)
        copiesTextView = view.findViewById(R.id.copiesTextView)
        rentButton = view.findViewById(R.id.rentButton)

        fetchBookDetails(bookId)

        rentButton.setOnClickListener {
            rentBook(bookId)
        }

        return view
    }

    private fun fetchBookDetails(bookId: Int) {
        val url = "http://10.0.2.2:5000/book/$bookId"
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                val title = response.getString("tytul")
                val autor = response.getString("autor")
                val category = response.getString("kategoria")
                val year = response.getInt("rok_wydania").toString()
                val copies = response.getInt("liczba_dostepnych_kopii").toString()


                titleTextView.text = "$title"
                authorTextView.text = "Autor: $autor"
                categoryTextView.text ="Kategoria: $category"
                yearTextView.text = "Rok wydania: $year"
                copiesTextView.text = copies
            },
            { error ->
                Log.e("BookDetailFragment", "Error fetching book details: ${error.message}")
            }
        )
        queue.add(jsonObjectRequest)
    }

    private fun rentBook(bookId: Int) {
        val url = "http://10.0.2.2:5000/rentals"
        val jsonObject = JSONObject()
        jsonObject.put("id_ksiazki", bookId)
        jsonObject.put("id_uzytkownika", UserSession.uid)
        jsonObject.put("data_wypozyczenia", LocalDate.now().toString())

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url, jsonObject,
            { response ->
                Toast.makeText(requireContext(), "Wypożyczono książkę!", Toast.LENGTH_SHORT).show()
                copiesTextView.text = (copiesTextView.text.toString().toInt() - 1).toString()
            },
            { error ->
                Log.e("BookDetailFragment", "Error renting book: ${error.message}")
                Toast.makeText(requireContext(), "Failed to rent book!", Toast.LENGTH_SHORT).show()
            }
        )
        queue.add(jsonObjectRequest)
    }
}
