package com.ichungelo.githubuser.helper

import androidx.recyclerview.widget.DiffUtil
import com.ichungelo.githubuser.model.UserItemResponse

class ProfileDiffCallback(
    private val mOldProfileList: List<UserItemResponse>,
    private val mNewProfileList: List<UserItemResponse>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return mOldProfileList.size
    }

    override fun getNewListSize(): Int {
        return mNewProfileList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldProfileList[oldItemPosition].id == mNewProfileList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldFavorite = mOldProfileList[oldItemPosition]
        val newFavorite = mNewProfileList[newItemPosition]
        return oldFavorite.avatarUrl == newFavorite.avatarUrl &&
                oldFavorite.htmlUrl == newFavorite.htmlUrl &&
                oldFavorite.login == newFavorite.login &&
                oldFavorite.type == newFavorite.type &&
                oldFavorite.url == newFavorite.url
    }
}