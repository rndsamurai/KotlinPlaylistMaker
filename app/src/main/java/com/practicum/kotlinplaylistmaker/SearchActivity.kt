package com.practicum.kotlinplaylistmaker

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SearchActivity : AppCompatActivity() {
    private lateinit var searchEditText: EditText
    private var searchQuery: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)


        val frameLayout = findViewById<FrameLayout>(R.id.container)
        val inputEditText = findViewById<EditText>(R.id.inputEditText)
        val clearButton = findViewById<ImageView>(R.id.clear_text)


            fun clearButtonVisibility(s: CharSequence?): Int {
                return if (s.isNullOrEmpty()) {
                    View.GONE
                } else {
                    View.VISIBLE
                }
            }

            inputEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    clearButton.visibility = clearButtonVisibility(s)
                    searchQuery = s?.toString()
                }

                override fun afterTextChanged(s: Editable?) {}
            })
            savedInstanceState?.let {
                searchQuery = it.getString("search_query")
                searchQuery?.let { query -> searchEditText.setText(query) }
            }
            clearButton.setOnClickListener {
                inputEditText.setText("")
            }
            fun onSaveInstanceState(outState : Bundle) {
                super.onSaveInstanceState(outState)
                searchQuery?.let { outState.putString("search_query", it) }
            }
            fun onRestoreInstanceState(savedInstanceState: Bundle) {
                super.onRestoreInstanceState(savedInstanceState)
                searchQuery = savedInstanceState.getString("search_query")
            }
        }
    }







