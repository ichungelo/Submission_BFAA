package com.ichungelo.githubuser.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProfileResponse(
    @field:SerializedName("items")
    val items: List<UserItemResponse>
) : Parcelable

@Entity
@Parcelize
data class UserItemResponse(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    @field:SerializedName("id")
    val id: Int,

    @ColumnInfo(name = "avatar_url")
    @field:SerializedName("avatar_url")
    val avatarUrl: String,

    @ColumnInfo(name = "html_url")
    @field:SerializedName("html_url")
    val htmlUrl: String,

    @ColumnInfo(name = "login")
    @field:SerializedName("login")
    val login: String,

    @ColumnInfo(name = "type")
    @field:SerializedName("type")
    val type: String,

    @ColumnInfo(name = "url")
    @field:SerializedName("url")
    val url: String
) : Parcelable

@Parcelize
data class DetailResponse(

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("followers")
    val followers: Int,

    @field:SerializedName("avatar_url")
    val avatarUrl: String,

    @field:SerializedName("html_url")
    val htmlUrl: String,

    @field:SerializedName("following")
    val following: Int,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("company")
    val company: String,

    @field:SerializedName("location")
    val location: String,

    @field:SerializedName("public_repos")
    val publicRepos: Int,

    @field:SerializedName("login")
    val login: String
) : Parcelable