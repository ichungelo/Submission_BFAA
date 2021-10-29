package com.ichungelo.githubuser.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.ichungelo.githubuser.R
import com.ichungelo.githubuser.fragment.SettingFragment
import com.ichungelo.githubuser.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener(this)

        supportFragmentManager.beginTransaction().add(R.id.settings_holder, SettingFragment())
            .commit()
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btn_back -> onBackPressed()
        }
    }
}