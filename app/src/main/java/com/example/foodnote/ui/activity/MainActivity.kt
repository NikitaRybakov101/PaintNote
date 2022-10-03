package com.example.foodnote.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.foodnote.R
import com.example.foodnote.databinding.ActivityMainBinding
import com.example.foodnote.utils.hide
import com.example.foodnote.utils.show

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initNavigation()
    }

    private fun initNavigation() {
        val navView = binding.bottomNavigation
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.container_root) as NavHostFragment
        val navController = navHostFragment.navController

        navView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.splashScreenFragment -> {
                    binding.bottomNavigation.hide()
                }
                else -> binding.bottomNavigation.show()
            }
        }
    }

}

