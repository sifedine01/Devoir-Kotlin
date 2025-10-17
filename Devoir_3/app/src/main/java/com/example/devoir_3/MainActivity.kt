package com.example.devoir_3

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var bookService: BookService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val books = BookService().getBooks()
        displayBooks(books)
    }

    private fun displayBooks(books: List<Book>) {
        val textView = findViewById<TextView>(R.id.textViewBooks)
        val displayText = books.joinToString("\n") { "${it.title} - ${it.author}" }
        textView.text = displayText
    }
}