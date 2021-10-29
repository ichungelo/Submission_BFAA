package com.ichungelo.githubuser.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ichungelo.githubuser.helper.Event
import com.ichungelo.githubuser.api.ApiConfig
import com.ichungelo.githubuser.model.UserItemResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowersViewModel : ViewModel() {
    private val _followers = MutableLiveData<List<UserItemResponse>>()
    val followers: LiveData<List<UserItemResponse>> = _followers

    private val _errorFollowers = MutableLiveData<Event<String>>()
    val errorFollowers: LiveData<Event<String>> = _errorFollowers

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    fun setDetailPager(username: String) {
        val client = ApiConfig.getApiService().getFollowers(username)
        client.enqueue(object : Callback<List<UserItemResponse>> {
            override fun onResponse(
                call: Call<List<UserItemResponse>>,
                response: Response<List<UserItemResponse>>
            ) {
                _loading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _followers.value = (responseBody)
                    }
                } else {
                    _loading.value = false
                    Log.d(TAG, "onFailure: ${response.message()}")
                    _errorFollowers.value = Event("Following Fragment\n${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<UserItemResponse>>, t: Throwable) {
                _loading.value = false
                Log.d(TAG, "onFailure: ${t.message}")
                _errorFollowers.value = Event("Following Fragment\n${t.message}")
            }
        })
    }

    companion object {
        private val TAG = FollowersViewModel::class.java.simpleName
    }
}