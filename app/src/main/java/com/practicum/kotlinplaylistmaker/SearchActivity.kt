package com.practicum.kotlinplaylistmaker

import android.app.Activity
import android.content.Intent
import android.hardware.SensorAdditionalInfo
import android.os.Bundle
import android.os.Message
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
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.sql.Time
import kotlin.random.Random

class SearchActivity : AppCompatActivity() {
    private lateinit var searchEditText: EditText
    private lateinit var contentContainer: FrameLayout
    private lateinit var placeholderMessage: TextView
    private lateinit var tracklistErrorView: View
    private lateinit var refresh: Button
    private var searchQuery: String? = null
    private val iTunesBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val iTunesService = retrofit.create(ITunesApi::class.java)
    private val tracks = ArrayList<Track>()
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

        val recycler = findViewById<RecyclerView>(R.id.trackList)
        val tracksArray = ArrayList<Track>()
        val inputEditText = findViewById<EditText>(R.id.inputEditText)
        val clearButton = findViewById<ImageView>(R.id.clear_text)
        val backButton = findViewById<ImageButton>(R.id.back_button)
        tracklistErrorView = layoutInflater.inflate(R.layout.tracklist_error, null)
        refresh = tracklistErrorView.findViewById(R.id.refresh)
        contentContainer = findViewById<FrameLayout>(R.id.pomogite)
        placeholderMessage = findViewById<TextView>(R.id.placeholdermessage)
        adapter.tracks = tracks
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = TrackAdapter(tracksArray)

        fun showErrorView() {
            val errorView = layoutInflater.inflate(R.layout.tracklist_error, null)
            contentContainer.removeAllViews()
            contentContainer.addView(errorView)
        }

        fun showNothingView() {
            val nothingView = layoutInflater.inflate(R.layout.tracklist_nothing, null)
            contentContainer.removeAllViews()
            contentContainer.addView(nothingView)
        }

        fun showRecyclerView() {
            contentContainer.removeAllViews()
            contentContainer.addView(recycler)
        }

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                iTunesService.searchTracks(inputEditText.text.toString())
                    .enqueue(object : Callback<TracksResponse> {
                        override fun onResponse(
                            call: Call<TracksResponse?>,
                            response: Response<TracksResponse?>
                        ) {
                            if (response.code() == 200) {
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
                    })
                true
            } else {
                false
            }
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
                contentContainer.removeAllViews()
            }
        }
    }












