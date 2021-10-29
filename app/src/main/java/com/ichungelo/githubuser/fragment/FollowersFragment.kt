package com.ichungelo.githubuser.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ichungelo.githubuser.model.UserItemResponse
import com.ichungelo.githubuser.adapter.ProfileAdapter
import com.ichungelo.githubuser.databinding.FragmentFollowersBinding
import com.ichungelo.githubuser.viewmodel.FollowersViewModel
import com.shashank.sony.fancytoastlib.FancyToast

class FollowersFragment : Fragment() {
    private lateinit var followersViewModel: FollowersViewModel
    private lateinit var adapter: ProfileAdapter
    private lateinit var _binding: FragmentFollowersBinding
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        followersViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            FollowersViewModel::class.java
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBarFollowersVisibility(true)
        noFollowersVisibility(false)
        getUsername()
        getFollowersViewModel()
    }

    private fun getUsername() {
        val extraFollowers =
            activity?.intent?.getParcelableExtra<UserItemResponse>(EXTRA_PROFILE) as UserItemResponse
        followersViewModel.setDetailPager(extraFollowers.login)
    }

    private fun getFollowersViewModel() {
        with(followersViewModel) {
            followers.observe(viewLifecycleOwner, { followersItems ->
                if (followersItems != null) {
                    showRecyclerFollowers(followersItems)
                }
            })
            errorFollowers.observe(viewLifecycleOwner, {
                it.getContentIfNotHandled()?.let { result ->
                    makeToastError(result)
                }
            })
            loading.observe(viewLifecycleOwner, {
                progressBarFollowersVisibility(it)
            })
        }
    }

    private fun showRecyclerFollowers(item: List<UserItemResponse>) {
        adapter = ProfileAdapter()
        adapter.setListProfile(item)
        with(binding) {
            rvFollowers.layoutManager = LinearLayoutManager(activity)
            rvFollowers.adapter = adapter
            rvFollowers.setHasFixedSize(true)
        }
        if (adapter.itemCount == 0) {
            noFollowersVisibility(true)
        }
    }

    private fun progressBarFollowersVisibility(boolean: Boolean) {
        binding.progressHorizontalFollowers.visibility =
            if (boolean) View.VISIBLE else View.GONE
    }

    private fun noFollowersVisibility(boolean: Boolean) {
        if (boolean) {
            with(binding) {
                tvNoFollowers.visibility = View.VISIBLE
                imgNoResult.visibility = View.VISIBLE
            }
        } else {
            with(binding) {
                tvNoFollowers.visibility = View.GONE
                imgNoResult.visibility = View.GONE
            }
        }
    }

    private fun makeToastError(message: String) {
        FancyToast.makeText(activity, message, FancyToast.LENGTH_SHORT, FancyToast.ERROR, false)
            .show()
    }

    companion object {
        const val EXTRA_PROFILE = "extra_profile"
    }
}