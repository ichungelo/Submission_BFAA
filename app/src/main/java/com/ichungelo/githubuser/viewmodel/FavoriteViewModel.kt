package com.ichungelo.githubuser.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ichungelo.githubuser.model.UserItemResponse
import com.ichungelo.githubuser.repository.FavoriteRepository

class FavoriteViewModel(application: Application) : ViewModel() {
    private val mFavoriteRepository: FavoriteRepository = FavoriteRepository(application)

    fun getAllFavorite(): LiveData<List<UserItemResponse>> = mFavoriteRepository.getAllFavorites()
}