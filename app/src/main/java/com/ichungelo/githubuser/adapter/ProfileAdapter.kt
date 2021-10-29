package com.ichungelo.githubuser.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.ichungelo.githubuser.R
import com.ichungelo.githubuser.model.UserItemResponse
import com.ichungelo.githubuser.activity.DetailActivity
import com.ichungelo.githubuser.databinding.ItemProfileBinding
import com.ichungelo.githubuser.helper.ProfileDiffCallback

class ProfileAdapter : RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder>() {
    private val listProfile = ArrayList<UserItemResponse>()

    fun setListProfile(listProfile: List<UserItemResponse>) {
        val diffCallback = ProfileDiffCallback(this.listProfile, listProfile)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listProfile.clear()
        this.listProfile.addAll(listProfile)
        diffResult.dispatchUpdatesTo(this)
    }

    class ProfileViewHolder(private val binding: ItemProfileBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(userItemResponse: UserItemResponse) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(userItemResponse.avatarUrl)
                    .apply(RequestOptions().override(150, 150))
                    .placeholder(R.drawable.pict_not_found)
                    .error(R.drawable.pict_not_found)
                    .into(imgProfileAvatar)
                tvProfileUsername.text = userItemResponse.login
                tvProfileType.text = userItemResponse.type
            }
            itemView.setOnClickListener {
                val moveWithObjectIntent = Intent(itemView.context, DetailActivity::class.java)
                moveWithObjectIntent
                    .putExtra(DetailActivity.EXTRA_PROFILE, userItemResponse)
                itemView.context.startActivity(moveWithObjectIntent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val binding = ItemProfileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProfileViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        holder.bind(listProfile[position])
    }

    override fun getItemCount(): Int = listProfile.size
}