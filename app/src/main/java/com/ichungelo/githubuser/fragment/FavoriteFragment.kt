package com.ichungelo.githubuser.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ichungelo.githubuser.R
import com.ichungelo.githubuser.adapter.ProfileAdapter
import com.ichungelo.githubuser.databinding.FragmentFavoriteBinding
import com.ichungelo.githubuser.model.UserItemResponse
import com.ichungelo.githubuser.viewmodel.FavoriteViewModel
import com.ichungelo.githubuser.viewmodel.FavoriteViewModelFactory

class FavoriteFragment : Fragment() {
    private lateinit var _binding: FragmentFavoriteBinding
    private val binding get() = _binding
    private lateinit var adapter: ProfileAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoriteBackgroundVisibility(false)
        getFavoriteViewModel()
    }

    private fun getFavoriteViewModel() {
        obtainViewModel().getAllFavorite().observe(this, { favoriteList ->
            if (favoriteList != null) {
                showRecyclerFavorite(favoriteList)
            }
        })
    }

    private fun obtainViewModel(): FavoriteViewModel {
        val factory = FavoriteViewModelFactory.getInstance(activity!!.application)
        return ViewModelProvider(activity!!, factory).get(FavoriteViewModel::class.java)
    }

    private fun showRecyclerFavorite(item: List<UserItemResponse>) {
        when {
            item.size > 1 -> {
                binding.tvFavoriteBanner.text = resources.getText(R.string.your_favorite_users_plural)
            }
            item.size == 1 -> {
                binding.tvFavoriteBanner.text = resources.getText(R.string.your_favorite_users_singular)
            }
            else -> {
                binding.tvFavoriteBanner.visibility = View.GONE
            }
        }
        adapter = ProfileAdapter()
        adapter.setListProfile(item)
        with(binding) {
            rvFavorite.layoutManager = LinearLayoutManager(activity)
            rvFavorite.adapter = adapter
            rvFavorite.setHasFixedSize(true)
        }
        if (adapter.itemCount == 0) {
            favoriteBackgroundVisibility(true)
        }
    }

    private fun favoriteBackgroundVisibility(boolean: Boolean) {
        if (boolean) {
            with(binding) {
                tvNoFavorite.visibility = View.VISIBLE
                imgNoFavorite.visibility = View.VISIBLE
            }
        } else {
            with(binding) {
                tvNoFavorite.visibility = View.GONE
                imgNoFavorite.visibility = View.GONE
            }
        }
    }
}