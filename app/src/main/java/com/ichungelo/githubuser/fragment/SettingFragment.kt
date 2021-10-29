package com.ichungelo.githubuser.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.ichungelo.githubuser.R
import com.ichungelo.githubuser.helper.SettingPreferences
import com.ichungelo.githubuser.viewmodel.SettingViewModel
import com.ichungelo.githubuser.viewmodel.SettingViewModelFactory

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "setting")

class SettingFragment : PreferenceFragmentCompat() {
    private var darkModePreference: SwitchPreference? = null
    private var localizationPreference: Preference? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preference)

        darkModePreference = findPreference(resources.getString(R.string.key_dark_theme))
        localizationPreference = findPreference(resources.getString(R.string.key_localization))

        val pref = SettingPreferences.getInstance(requireContext().dataStore)
        val settingViewModel = ViewModelProvider(this, SettingViewModelFactory(pref)).get(
            SettingViewModel::class.java
        )

        darkModePreference?.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { _, _ ->
                if (darkModePreference?.isChecked!!) {
                    settingViewModel.saveThemeSettings(false)
                    darkModePreference?.isChecked = false
                } else {
                    settingViewModel.saveThemeSettings(true)
                    darkModePreference?.isChecked = true
                }
                false
            }

        settingViewModel.getThemeSettings().observe(this,
            { isDarkModeActive: Boolean ->
                if (isDarkModeActive) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            })

        localizationPreference?.onPreferenceClickListener =
            Preference.OnPreferenceClickListener {
                val localization = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(localization)
                false
            }
    }
}