package com.ichungelo.githubuser.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.ichungelo.githubuser.databinding.ActivitySplashBinding
import com.ichungelo.githubuser.fragment.dataStore
import com.ichungelo.githubuser.helper.SettingPreferences
import com.ichungelo.githubuser.viewmodel.SettingViewModel
import com.ichungelo.githubuser.viewmodel.SettingViewModelFactory

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val splashStatusBarColor = Color.rgb(106, 27, 154)
        val splashNavigationBarColor = Color.rgb(230, 61, 118)
        window.statusBarColor = splashStatusBarColor
        window.navigationBarColor = splashNavigationBarColor

        sleepSplash()
        getThemeSettings()
    }

    private fun getThemeSettings() {
        val pref = SettingPreferences.getInstance(dataStore)
        val settingViewModel = ViewModelProvider(this, SettingViewModelFactory(pref)).get(
            SettingViewModel::class.java
        )
        settingViewModel.getThemeSettings().observe(this,
            { isDarkModeActive: Boolean ->
                if (isDarkModeActive) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            })
    }

    private fun sleepSplash() {
        val sleepDuration = 3000L
        val background = object : Thread() {
            override fun run() {
                try {
                    sleep(sleepDuration)
                    val intent = Intent(baseContext, MainActivity::class.java)
                    startActivity(intent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        background.start()
    }
}