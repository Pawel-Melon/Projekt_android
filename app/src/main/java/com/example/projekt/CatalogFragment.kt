package com.example.projekt

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.projekt.Book
class CatalogFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var sortSpinner: Spinner
    private lateinit var queue: RequestQueue
    private lateinit var booksAdapter: BooksAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_catalog, container, false)
        recyclerView = view.findViewById(R.id.booksRecyclerView)
        sortSpinner = view.findViewById(R.id.sortSpinner)

        recyclerView.layoutManager = LinearLayoutManager(context)
        booksAdapter = BooksAdapter(listOf()) { book ->
            val bundle = Bundle().apply {
                putInt("bookId", book.id)
            }
            findNavController().navigate(R.id.action_catalogFragment_to_bookDetailFragment, bundle)
        }
        recyclerView.adapter = booksAdapter

        queue = Volley.newRequestQueue(requireContext())
        fetchBooks()

        sortSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                sortBooks(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        return view
    }


    private fun fetchBooks() {
        val url = "http://10.0.2.2:5000/books"
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                val books = mutableListOf<Book>()
                for (i in 0 until response.length()) {
                    val bookJson = response.getJSONObject(i)
                    val book = Book(
                        id = bookJson.getInt("id"),
                        tytul = bookJson.getString("tytul"),
                        autor = bookJson.getString("autor"),
                        kategoria = bookJson.getString("kategoria"),
                        rok_wydania = bookJson.getInt("rok_wydania"),
                        liczba_dostepnych_kopii = bookJson.getInt("liczba_dostepnych_kopii")
                    )
                    books.add(book)
                }
                booksAdapter.updateBooks(books)
            },
            { error ->
                Log.e("CatalogFragment", "Error fetching books: ${error.message}")
            }
        )
        queue.add(jsonArrayRequest)
    }

    private fun sortBooks(criteria: Int) {
        val sortedBooks = when (criteria) {
            0 -> booksAdapter.books.sortedBy { it.tytul }
            1 -> booksAdapter.books.sortedBy { it.kategoria }
            2 -> booksAdapter.books.sortedBy { it.autor }
            else -> booksAdapter.books
        }
        booksAdapter.updateBooks(sortedBooks)
    }
}
