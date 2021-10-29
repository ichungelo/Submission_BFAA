package com.ichungelo.githubuser.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ichungelo.githubuser.R
import com.ichungelo.githubuser.adapter.ProfileAdapter
import com.ichungelo.githubuser.databinding.FragmentSearchBinding
import com.ichungelo.githubuser.model.UserItemResponse
import com.ichungelo.githubuser.viewmodel.ProfileViewModel
import com.shashank.sony.fancytoastlib.FancyToast

class SearchFragment : Fragment() {
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var adapter: ProfileAdapter
    private lateinit var _binding: FragmentSearchBinding
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        profileViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            ProfileViewModel::class.java
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressBarDetailVisibility(false)

        searchUser()
        getProfileViewModel()
    }

    private fun backgroundVisibility(boolean: Boolean) {
        if (boolean) {
            with(binding) {
                bgSearchText.visibility = View.VISIBLE
                bgSearchImg.visibility = View.VISIBLE
            }
        } else {
            with(binding) {
                bgSearchText.visibility = View.GONE
                bgSearchImg.visibility = View.GONE
            }
        }
    }

    private fun progressBarDetailVisibility(boolean: Boolean) {
        binding.progressBar.visibility = if (boolean) View.VISIBLE else View.GONE
    }

    private fun searchUser() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String): Boolean {
                backgroundVisibility(false)
                profileViewModel.setProfile(p0)
                return true
            }

            override fun onQueryTextChange(p0: String): Boolean {
                return false
            }
        })
    }

    private fun showRecyclerList(item: List<UserItemResponse>) {
        adapter = ProfileAdapter()
        adapter.setListProfile(item)
        with(binding) {
            rvProfile.layoutManager = LinearLayoutManager(activity)
            rvProfile.adapter = adapter
            rvProfile.setHasFixedSize(true)
        }
        if (adapter.itemCount == 0) {
            backgroundVisibility(true)
            binding.bgSearchText.text = resources.getText(R.string.no_result_found)
            binding.bgSearchImg.setImageResource(R.drawable.bg_search_not_found)
        }
    }

    private fun getProfileViewModel() {
        with(profileViewModel) {
            profile.observe(viewLifecycleOwner, { result ->
                if (result != null) {
                    backgroundVisibility(false)
                    showRecyclerList(result)
                }
            })
            activity?.let {
                errorProfile.observe(it, { event ->
                    event.getContentIfNotHandled()?.let { result ->
                        backgroundVisibility(true)
                        makeToastError(result)
                    }
                })
            }
            loading.observe(viewLifecycleOwner, {
                progressBarDetailVisibility(it)
            })
        }
    }

    private fun makeToastError(message: String) {
        FancyToast.makeText(activity, message, FancyToast.LENGTH_SHORT, FancyToast.ERROR, false)
            .show()
    }
}