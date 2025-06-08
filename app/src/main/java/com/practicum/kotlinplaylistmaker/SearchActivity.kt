package com.practicum.kotlinplaylistmaker

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentContainer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule

class SearchActivity : AppCompatActivity() {
    private lateinit var searchEditText: EditText
    private lateinit var refresh: Button
    private var searchQuery: String? = null
    private val iTunesBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val iTunesService = retrofit.create(ITunesApi::class.java)
    private val tracks = mutableListOf<Track>()
    private val adapter = TrackAdapter(tracks)


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        searchQuery?.let { outState.putString("search_query", it) }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchQuery = savedInstanceState.getString("search_query")
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        val tracksArray = ArrayList<Track>()
        val inputEditText = findViewById<EditText>(R.id.inputEditText)
        val clearButton = findViewById<ImageView>(R.id.clear_text)
        val backButton = findViewById<ImageButton>(R.id.back_button)
        val errorView = findViewById<View>(R.id.errorView)
        val nothingView = findViewById<View>(R.id.nothingView)
        val recycler = findViewById<RecyclerView>(R.id.trackList)
        refresh = errorView.findViewById(R.id.refresh)
        adapter.tracks = tracks
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = TrackAdapter(tracksArray)

        fun showErrorView() {
            errorView.isVisible = true
            recycler.isVisible = false
            nothingView.isVisible = false
        }
        fun showNothingView() {
            errorView.isVisible = false
            recycler.isVisible = false
            nothingView.isVisible = true
        }
        fun showRecyclerView() {
            errorView.isVisible = false
            recycler.isVisible = true
            nothingView.isVisible = false
        }


        fun startSearch() {iTunesService.searchTracks(inputEditText.text.toString())
            .enqueue(object : Callback<TracksResponse> {
                override fun onResponse(
                    call: Call<TracksResponse?>,
                    response: Response<TracksResponse?>
                ) {
                    if (response.isSuccessful) {
                        tracksArray.clear()
                        if (response.body()?.results?.isNotEmpty() == true) {
                            tracksArray.addAll(response.body()?.results!!)
                            adapter.notifyDataSetChanged()
                        }
                        if (tracksArray.isEmpty()) {
                            showNothingView()
                        } else {
                            showRecyclerView()
                        }
                    } else {
                        showErrorView()
                        response.code().toString()
                    }
                }

                override fun onFailure(
                    call: Call<TracksResponse?>,
                    t: Throwable
                ) {
                    showErrorView()
                    t.message.toString()
                }
            })}

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                startSearch()
                true
            } else {
                false
            }
        }
        refresh.setOnClickListener {
            startSearch()
        }


            backButton.setOnClickListener {
                finish()
            }

            fun hideKeyboard(view: View) {
                val inputMethodManager =
                    getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            }

            fun clearButtonVisibility(s: CharSequence?): Boolean {
                return !s.isNullOrEmpty()
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
                    clearButton.isVisible = clearButtonVisibility(s)
                    searchQuery = s?.toString()
                }

                override fun afterTextChanged(s: Editable?) {

                }
            })
            savedInstanceState?.let {
                searchQuery = it.getString("search_query")
                searchQuery?.let { query -> searchEditText.setText(query) }
            }
            clearButton.setOnClickListener {
                inputEditText.setText("")
                hideKeyboard(inputEditText)
                errorView.isVisible = false
                recycler.isVisible = false
                nothingView.isVisible = false

            }
        }
    }

