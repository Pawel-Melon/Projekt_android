package com.example.projekt
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BooksAdapter(internal var books: List<Book>, private val clickListener: (Book) -> Unit) : RecyclerView.Adapter<BooksAdapter.BookViewHolder>() {

    fun updateBooks(newBooks: List<Book>) {
        books = newBooks
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.book_item, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = books[position]
        holder.bind(book, clickListener)
    }

    override fun getItemCount() = books.size

    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        private val authorTextView: TextView = itemView.findViewById(R.id.authorTextView)
        private val CatTextView: TextView = itemView.findViewById(R.id.categTextView)


        fun bind(book: Book, clickListener: (Book) -> Unit) {
            val aut=book.autor
            val kat=book.kategoria
            titleTextView.text = book.tytul
            authorTextView.text = "Autor: $aut"
            CatTextView.text= "Kategoria: $kat"
            itemView.setOnClickListener { clickListener(book) }
        }
    }
}
