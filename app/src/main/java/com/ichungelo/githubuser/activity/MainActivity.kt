package com.ichungelo.githubuser.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ichungelo.githubuser.*
import com.ichungelo.githubuser.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnSettings.setOnClickListener(this)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navView: BottomNavigationView = binding.navView

        navController = navHostFragment.navController
        navView.setupWithNavController(navController)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btn_settings -> {
                val openSettings = Intent(this, SettingsActivity::class.java)
                startActivity(openSettings)
            }
        }
    }
}