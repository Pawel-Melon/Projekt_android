package com.example.projekt

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
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
    private lateinit var imageView: ImageView
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
        imageView = view.findViewById(R.id.imageView)

        fetchBookDetails(bookId)

        rentButton.setOnClickListener {
            rentBook(bookId)
        }

        return view
    }

    private fun fetchBookDetails(bookId: Int) {
        val url = "http://192.168.1.17:5000/book/$bookId"
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

                val imageResId = when (bookId) {
                    1 -> R.drawable.ostatnie_zyczenie
                    2 -> R.drawable.hobbit
                    3 -> R.drawable.kodleonarda_da_vinci
                    4 -> R.drawable.buszujacy_w_zbozu
                    5 -> R.drawable.przygody_sherlocka_holmesa
                    6 -> R.drawable.harry_potter_kamien_filozoficzny
                    7 -> R.drawable.ania_z_zielonego_wzgorza
                    8 -> R.drawable.wielki_gatsby
                    9 -> R.drawable.opowiesc_o_dwoch_miastach
                    10 -> R.drawable.zabic_drozda

                    else -> R.drawable.diuna // Domyślna okładka, jeśli bookId nie jest znane
                }
                imageView.setImageResource(imageResId)
            },
            { error ->
                Log.e("BookDetailFragment", "Error fetching book details: ${error.message}")
            }
        )
        queue.add(jsonObjectRequest)
    }

    private fun rentBook(bookId: Int) {
        val url = "http://192.168.1.17:5000/rentals"
        val jsonObject = JSONObject()
        jsonObject.put("id_ksiazki", bookId)
        jsonObject.put("id_uzytkownika", UserSession.uid)
        jsonObject.put("data_wypozyczenia", LocalDate.now().toString())

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url, jsonObject,
            { response ->
                val message = response.getString("message")
                if (message == "Brak srodkow!") {
                    Toast.makeText(context, "Brak środków na koncie!", Toast.LENGTH_SHORT).show()
                } else {

                Toast.makeText(requireContext(), "Wypożyczono książkę!", Toast.LENGTH_SHORT).show()
                copiesTextView.text = (copiesTextView.text.toString().toInt() - 1).toString()}
            },
            { error ->
                Log.e("BookDetailFragment", "Error renting book: ${error.message}")
                Toast.makeText(requireContext(), "Brak dostępnych egzemplarzy!", Toast.LENGTH_SHORT).show()
            }
        )
        queue.add(jsonObjectRequest)
    }
}
