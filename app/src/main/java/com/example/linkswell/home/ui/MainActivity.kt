package com.example.linkswell.home.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.linkswell.LinksWellApplication
import com.example.linkswell.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        (application as LinksWellApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}