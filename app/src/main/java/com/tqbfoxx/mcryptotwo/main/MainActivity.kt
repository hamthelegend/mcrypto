package com.tqbfoxx.mcryptotwo.main

import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.tqbfoxx.mcryptotwo.R
import com.tqbfoxx.mcryptotwo.databinding.ActivityMainBinding
import com.tqbfoxx.mcryptotwo.extensions.setBindingContentView
import com.tqbfoxx.mcryptotwo.main.fragments.NavigationDrawerFragment

class MainActivity : AppCompatActivity() {

    // Binding Object
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // sets content view
        // binding = DataBindingUtil.setContentView<T>(this, R.layout.activity_main)
        binding = setBindingContentView(R.layout.activity_main)


        val metropolisReg = ResourcesCompat.getFont(this, R.font.quicksand_regular)
        val metropolisMed = ResourcesCompat.getFont(this, R.font.quicksand_medium)

        binding.toolbarLayout.apply {
            setExpandedTitleTypeface(metropolisReg)
            setCollapsedTitleTypeface(metropolisMed)
        }

        NavigationUI.setupWithNavController(
            binding.toolbarLayout,
            binding.toolbar,
            this.findNavController(R.id.fragment_host)
        )

        setSupportActionBar(binding.bottomAppBar!!)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_nav_drawer, menu)
        return true
    }

    fun showMessageAppBar() {
        binding.messageAppBar.visibility = View.VISIBLE
        binding.bottomAppBar?.visibility = View.GONE
    }

    fun hideMessageAppBar() {
        binding.messageAppBar.visibility = View.GONE
        binding.bottomAppBar?.visibility = View.VISIBLE
    }

    fun showFAB() {
        binding.fab.show()
    }

    fun hideFAB() {
        binding.fab.hide()
    }

    fun collapseAppBar() {
        binding.appBar.setExpanded(false)
    }

    fun showNavDrawer() {
        val navDrawerFragment = NavigationDrawerFragment()
        navDrawerFragment.show(supportFragmentManager, navDrawerFragment.tag)
    }

}
