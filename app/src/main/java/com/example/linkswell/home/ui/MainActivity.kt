package com.example.linkswell.home.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.linkswell.LinksWellApplication
import com.example.linkswell.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as LinksWellApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }


    private fun getTextFromIntent(intent: Intent): String {
        return intent.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT)?.toString()!!
    }
}