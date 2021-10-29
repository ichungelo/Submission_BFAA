package com.ichungelo.githubuser.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ichungelo.githubuser.helper.Event
import com.ichungelo.githubuser.model.UserItemResponse
import com.ichungelo.githubuser.api.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowingViewModel : ViewModel() {
    private val _following = MutableLiveData<List<UserItemResponse>>()
    val following: LiveData<List<UserItemResponse>> = _following

    private val _errorFollowing = MutableLiveData<Event<String>>()
    val errorFollowing: LiveData<Event<String>> = _errorFollowing

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    fun setDetailPager(username: String) {
        val client = ApiConfig.getApiService().getFollowing(username)
        client.enqueue(object : Callback<List<UserItemResponse>> {
            override fun onResponse(
                call: Call<List<UserItemResponse>>,
                response: Response<List<UserItemResponse>>
            ) {
                _loading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _following.value = (responseBody)
                    }
                } else {
                    _loading.value = false
                    Log.d(TAG, "onFailure: ${response.message()}")
                    _errorFollowing.value = Event("Following Fragment\n${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<UserItemResponse>>, t: Throwable) {
                _loading.value = false
                Log.d(TAG, "onFailure: ${t.message}")
                _errorFollowing.value = Event("Following Fragment\n${t.message}")
            }
        })
    }

    companion object {
        private val TAG = FollowingViewModel::class.java.simpleName
    }
}