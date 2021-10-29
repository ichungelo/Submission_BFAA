package com.ichungelo.githubuser.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ichungelo.githubuser.model.UserItemResponse

@Dao
interface FavoriteDao {

    @Insert
    fun insert(userItemResponse: UserItemResponse)

    @Delete
    fun delete(userItemResponse: UserItemResponse)

    @Query("SELECT * from userItemResponse ORDER BY login ASC")
    fun getAllFavorites(): LiveData<List<UserItemResponse>>

    @Query("SELECT * from userItemResponse WHERE id LIKE :id")
    fun getId(id: Int): LiveData<UserItemResponse>
}