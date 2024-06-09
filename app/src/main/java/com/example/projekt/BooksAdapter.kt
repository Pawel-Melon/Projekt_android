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

        fun bind(book: Book, clickListener: (Book) -> Unit) {
            titleTextView.text = book.tytul
            authorTextView.text = book.autor
            itemView.setOnClickListener { clickListener(book) }
        }
    }
}
