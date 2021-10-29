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
import com.ichungelo.githubuser.databinding.FragmentFollowingBinding
import com.ichungelo.githubuser.viewmodel.FollowingViewModel
import com.shashank.sony.fancytoastlib.FancyToast

class FollowingFragment : Fragment() {
    private lateinit var followingViewModel: FollowingViewModel
    private lateinit var adapter: ProfileAdapter
    private lateinit var _binding: FragmentFollowingBinding
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        followingViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            FollowingViewModel::class.java
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBarFollowingVisibility(true)
        noFollowingVisibility(false)
        getUsername()
        getFollowingViewModel()
    }

    private fun getUsername() {
        val extraFollowing =
            activity?.intent?.getParcelableExtra<UserItemResponse>(EXTRA_PROFILE) as UserItemResponse
        followingViewModel.setDetailPager(extraFollowing.login)
    }

    private fun getFollowingViewModel() {
        with(followingViewModel) {
            following.observe(viewLifecycleOwner, { followingItems ->
                if (followingItems != null) {
                    showRecyclerFollowing(followingItems)
                }
            })
            errorFollowing.observe(viewLifecycleOwner, {
                it.getContentIfNotHandled()?.let { result ->
                    makeToastError(result)
                }
            })
            loading.observe(viewLifecycleOwner, {
                progressBarFollowingVisibility(it)
            })
        }
    }

    private fun showRecyclerFollowing(item: List<UserItemResponse>) {
        adapter = ProfileAdapter()
        adapter.setListProfile(item)
        with(binding) {
            rvFollowing.layoutManager = LinearLayoutManager(activity)
            rvFollowing.adapter = adapter
            rvFollowing.setHasFixedSize(true)
        }
        if (adapter.itemCount == 0) {
            noFollowingVisibility(true)
        }
    }

    private fun progressBarFollowingVisibility(boolean: Boolean) {
        binding.progressHorizontalFollowing.visibility =
            if (boolean) View.VISIBLE else View.GONE
    }

    private fun noFollowingVisibility(boolean: Boolean) {
        if (boolean) {
            with(binding) {
                tvNoFollowing.visibility = View.VISIBLE
                imgNoResult.visibility = View.VISIBLE
            }
        } else {
            with(binding) {
                tvNoFollowing.visibility = View.GONE
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