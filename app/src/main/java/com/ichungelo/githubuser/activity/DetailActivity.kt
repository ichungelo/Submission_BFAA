package com.ichungelo.githubuser.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.ichungelo.githubuser.model.DetailResponse
import com.ichungelo.githubuser.model.UserItemResponse
import com.ichungelo.githubuser.R
import com.ichungelo.githubuser.adapter.SectionPagerAdapter
import com.ichungelo.githubuser.databinding.ActivityDetailBinding
import com.ichungelo.githubuser.viewmodel.DetailViewModel
import com.ichungelo.githubuser.viewmodel.FavoriteViewModelFactory
import com.shashank.sony.fancytoastlib.FancyToast

class DetailActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var detailViewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnHome.setOnClickListener(this)
        binding.btnExternalLink.setOnClickListener(this)

        detailViewModel = obtainViewModel(this@DetailActivity)

        progressBarDetailVisibility(true)
        setViewPager()
        getUsername()
        getDetailViewModel()
        btnFavoriteBehaviour()
    }
    private fun profileExtra() = intent.getParcelableExtra<UserItemResponse>(EXTRA_PROFILE) as UserItemResponse

    private fun obtainViewModel(activity: AppCompatActivity): DetailViewModel {
        val factory = FavoriteViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(DetailViewModel::class.java)
    }

    private fun setViewPager() {
        val sectionsPagerAdapter = SectionPagerAdapter(this)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    private fun getUsername() {
        detailViewModel.setProfileDetail(profileExtra().login)
    }

    private fun getDetailViewModel() {
        with(detailViewModel) {
            detail.observe(this@DetailActivity, { result ->
                if (result != null) {
                    setDetail(result)
                    btnShareBehavior(result)
                }
            })
            errorDetail.observe(this@DetailActivity, {
                it.getContentIfNotHandled()?.let { result ->
                    makeToastError(result)
                }
            })
            loading.observe(this@DetailActivity, {
                progressBarDetailVisibility(it)
            })
        }
    }

    private fun setDetail(detailResponse: DetailResponse) {
        with(binding) {
            Glide
                .with(root)
                .load(detailResponse.avatarUrl)
                .apply(RequestOptions().override(150, 150))
                .placeholder(R.drawable.pict_not_found)
                .error(R.drawable.pict_not_found)
                .into(imgDetailAvatar)
            tvDetailName.text = detailResponse.name
            tvDetailFollowing.text = detailResponse.following.toString()
            tvDetailFollowers.text = detailResponse.followers.toString()
            tvDetailRepository.text = detailResponse.publicRepos.toString()
            tvDetailLocation.text = detailResponse.location
            tvDetailCompany.text = detailResponse.company
            tvDetailUsername.text = detailResponse.login
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btn_home -> {
                val startMain = Intent(this, MainActivity::class.java)
                startActivity(startMain)
            }
            R.id.btn_external_link -> {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_VIEW
                    data = Uri.parse(profileExtra().htmlUrl)
                }
                startActivity(sendIntent)
            }
        }
    }

    private fun progressBarDetailVisibility(boolean: Boolean) {
        binding.progressBarDetail.visibility =
            if (boolean) View.VISIBLE else View.GONE
    }

    private fun btnShareBehavior(detailResponse: DetailResponse) {
        binding.btnShare.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(
                    Intent.EXTRA_TEXT, """
                        GITHUB USER PROFILE
                        ${detailResponse.name.uppercase()}
                        
                        Username: ${detailResponse.login}
                        Location: ${detailResponse.location}
                        Company: ${detailResponse.company}
                        Repository: ${detailResponse.publicRepos}
                        Following: ${detailResponse.following}
                        Followers: ${detailResponse.followers}
                        link: ${detailResponse.htmlUrl}
                    """.trimIndent()
                )
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }
    }

    private fun btnFavoriteBehaviour() {
        detailViewModel.getId(profileExtra().id).observe(this@DetailActivity, { result ->
            if (result != null) {
                binding.btnFavorite.setOnClickListener {
                    detailViewModel.delete(profileExtra())
                    makeToastFavorite(resources.getString(R.string.remove_favorite))
                }
                binding.btnFavorite.setImageResource(R.drawable.ic_favorite_on)
            } else {
                binding.btnFavorite.setOnClickListener {
                    detailViewModel.insert(profileExtra())
                    makeToastFavorite(resources.getString(R.string.add_favorite))
                }
                binding.btnFavorite.setImageResource(R.drawable.ic_favorite_off)
            }
        })
    }

    private fun makeToastFavorite(message: String) {
        FancyToast.makeText(this, message,FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,false)
            .show()
    }

    private fun makeToastError(message: String) {
        FancyToast.makeText(this, message, FancyToast.LENGTH_SHORT, FancyToast.ERROR, false)
            .show()
    }

    companion object {
        const val EXTRA_PROFILE = "extra_profile"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.following,
            R.string.followers,
        )
    }
}