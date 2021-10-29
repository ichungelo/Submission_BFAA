package com.ichungelo.githubuser.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.ichungelo.githubuser.database.FavoriteDao
import com.ichungelo.githubuser.database.FavoriteRoomDatabase
import com.ichungelo.githubuser.model.UserItemResponse
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteRepository(application: Application) {
    private val mFavoritesDao: FavoriteDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteRoomDatabase.getDatabase(application)
        mFavoritesDao = db.favoriteDao()
    }

    fun getId(id: Int): LiveData<UserItemResponse> = mFavoritesDao.getId(id)

    fun getAllFavorites(): LiveData<List<UserItemResponse>> = mFavoritesDao.getAllFavorites()

    fun insert(userItemResponse: UserItemResponse) {
        executorService.execute { mFavoritesDao.insert(userItemResponse) }
    }

    fun delete(userItemResponse: UserItemResponse) {
        executorService.execute { mFavoritesDao.delete(userItemResponse) }
    }
}