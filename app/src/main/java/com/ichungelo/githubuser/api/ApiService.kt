package com.ichungelo.githubuser.api

import com.ichungelo.githubuser.BuildConfig
import com.ichungelo.githubuser.model.DetailResponse
import com.ichungelo.githubuser.model.UserItemResponse
import com.ichungelo.githubuser.model.ProfileResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiService {
    @GET("search/users")
    fun getProfile(@Query("q") username: String): Call<ProfileResponse>

    @GET("users/{username}")
    fun getDetail(@Path("username") username: String): Call<DetailResponse>

    @GET("users/{username}/followers")
    fun getFollowers(@Path("username") username: String): Call<List<UserItemResponse>>

    @GET("users/{username}/following")
    fun getFollowing(@Path("username") username: String): Call<List<UserItemResponse>>
}