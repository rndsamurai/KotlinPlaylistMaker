package com.practicum.kotlinplaylistmaker
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)

        val backbutton = findViewById<ImageButton>(R.id.back_button)
        backbutton.setOnClickListener{
            finish()
        }

        val button = findViewById<ImageButton>(R.id.sharebutton)
        button.setOnClickListener {
            val shareintent = Intent(Intent.ACTION_SEND)
            val subject = "https://practicum.yandex.ru/learn/android-developer-plus"
            shareintent.setType("text/plain")
            shareintent.putExtra(Intent.EXTRA_TEXT,subject)
            startActivity(Intent.createChooser(shareintent,"Поделиться с помощью"))
            }

        val switch = findViewById<SwitchCompat>(R.id.darkthemeswitch)
        val sharedPreferences = getSharedPreferences("Mode", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val nightMode = sharedPreferences.getBoolean("night",false)

        if(nightMode) {
            switch.isChecked = true
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }

        switch.setOnCheckedChangeListener{ buttonView, isChecked ->
            if (!isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                editor.putBoolean("night", false)
                editor.apply()
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                editor.putBoolean("night", true)
                editor.apply()
            }
        }
       val email = getString(R.string.email)
        val link = getString(R.string.link)
        val message = getString(R.string.message)
        val gratutude = getString(R.string.gratitude)

        val onemorebutton = findViewById<ImageButton>(R.id.useragreementbutton)
        onemorebutton.setOnClickListener{
            val url = Uri.parse(link)
            val intent = Intent(Intent.ACTION_VIEW, url)
            startActivity(intent)
            }
        val lastbutton = findViewById<ImageButton>(R.id.supportbutton)
        lastbutton.setOnClickListener{
            val sendintent = Intent(Intent.ACTION_SENDTO)
            sendintent.data = Uri.parse("mailto:")
            sendintent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            sendintent.putExtra(Intent.EXTRA_SUBJECT, gratutude)
            sendintent.putExtra(Intent.EXTRA_TEXT, message)
            startActivity(sendintent)
            }
        }


}
