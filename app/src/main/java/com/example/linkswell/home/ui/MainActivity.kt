package com.example.linkswell.home.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.linkswell.LinksWellApplication
import com.example.linkswell.R
import com.example.linkswell.databinding.ActivityMainBinding
import com.example.linkswell.di.AppComponent
import com.example.linkswell.textprocess.TextProcessHelper
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    lateinit var appComponent: AppComponent

    @Inject
    lateinit var textProcessHelper: TextProcessHelper

    private lateinit var navController: NavController

    private var textExtracted: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        appComponent = (application as LinksWellApplication).appComponent
        appComponent.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handleIncomingIntent(intent)
        setupNavigation()
        setupView()
    }

    private fun setupNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        val inflater = navController.navInflater
        val graph = inflater.inflate(R.navigation.nav_graph)

        textExtracted?.let {
            graph.setStartDestination(R.id.save_link_navigation)
            navController.setGraph(graph, bundleOf("originalLink" to it))
        } ?: run {
            graph.setStartDestination(R.id.linkGroupsFragment)
            navController.setGraph(graph, null)
        }

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when (destination.id) {
                R.id.saveLinkFragment -> {
                    binding.toolbar.isVisible = false
                }
                else -> {
                    binding.toolbar.isVisible = true
                }
            }
        }
    }

    private fun setupView() {
        binding.toolbar.setNavigationOnClickListener {
            val anythingPoppedOut = navController.popBackStack()
            if (!anythingPoppedOut) {
                finish()
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleIncomingIntent(intent)
    }

    private fun handleIncomingIntent(intent: Intent?) {
        if (intent != null && intent.action == Intent.ACTION_SEND && intent.type == "text/plain") {
            textExtracted = getTextFromIntent(intent)
        }
    }


    private fun getTextFromIntent(intent: Intent): String? {
        return intent.getCharSequenceExtra(Intent.EXTRA_TEXT)?.toString()
    }
}