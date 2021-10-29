package com.ichungelo.githubuser.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ichungelo.githubuser.api.ApiConfig
import com.ichungelo.githubuser.helper.Event
import com.ichungelo.githubuser.model.UserItemResponse
import com.ichungelo.githubuser.model.ProfileResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel : ViewModel() {
    private val _profile = MutableLiveData<List<UserItemResponse>>()
    val profile: LiveData<List<UserItemResponse>> = _profile

    private val _errorProfile = MutableLiveData<Event<String>>()
    val errorProfile: LiveData<Event<String>> = _errorProfile

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    fun setProfile(username: String) {
        _loading.value = true
        val client = ApiConfig.getApiService().getProfile(username)
        client.enqueue(object : Callback<ProfileResponse> {
            override fun onResponse(
                call: Call<ProfileResponse>,
                response: Response<ProfileResponse>
            ) {
                _loading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _profile.value = responseBody.items

                    }
                } else {
                    _loading.value = false
                    Log.d(TAG, "onFailure: ${response.message()}")
                    _errorProfile.value = Event("Search\n${response.message()}")
                }
            }

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                _loading.value = false
                Log.d(TAG, "onFailure: ${t.message}")
                _errorProfile.value = Event("Search\n${t.message}")
            }
        })
    }

    companion object {
        private val TAG = ProfileViewModel::class.java.simpleName
    }
}