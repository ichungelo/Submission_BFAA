package com.ichungelo.githubuser.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ichungelo.githubuser.model.DetailResponse
import com.ichungelo.githubuser.helper.Event
import com.ichungelo.githubuser.api.ApiConfig
import com.ichungelo.githubuser.model.UserItemResponse
import com.ichungelo.githubuser.repository.FavoriteRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application) : ViewModel() {
    private val _detail = MutableLiveData<DetailResponse?>()
    val detail: LiveData<DetailResponse?> = _detail

    private val _errorDetail = MutableLiveData<Event<String>>()
    val errorDetail: LiveData<Event<String>> = _errorDetail

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val mFavoriteRepository: FavoriteRepository = FavoriteRepository(application)

    fun insert(userItemResponse: UserItemResponse) {
        mFavoriteRepository.insert(userItemResponse)
    }

    fun delete(userItemResponse: UserItemResponse) {
        mFavoriteRepository.delete(userItemResponse)
    }

    fun getId(id: Int): LiveData<UserItemResponse> = mFavoriteRepository.getId(id)


    fun setProfileDetail(username: String) {
        val client = ApiConfig.getApiService().getDetail(username)
        client.enqueue(object : Callback<DetailResponse> {
            override fun onResponse(
                call: Call<DetailResponse>,
                response: Response<DetailResponse>
            ) {
                _loading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _detail.value = responseBody
                    }
                } else {
                    _loading.value = false
                    Log.d(TAG, "onFailure: ${response.message()}")
                    _errorDetail.value = Event("Detail Activity\n${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                _loading.value = false
                Log.d(TAG, "onFailure: ${t.message}")
                _errorDetail.value = Event("Detail Activity\n${t.message}")
            }
        })
    }

    companion object {
        private val TAG = DetailViewModel::class.java.simpleName
    }
}