package com.future.tailormade_profile.feature.profile.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.future.tailormade.util.image.ImageLoader
import com.future.tailormade_profile.R
import com.future.tailormade_profile.core.model.response.ProfileDesignResponse
import com.future.tailormade_profile.databinding.LayoutProfileDesignImageBinding

class ProfileDesignAdapter(private val onClickListener: (String) -> Unit) :
    ListAdapter<ProfileDesignResponse, ProfileDesignAdapter.ProfileDesignViewHolder>(diffCallback) {

  companion object {
    private val diffCallback = object : DiffUtil.ItemCallback<ProfileDesignResponse>() {

      override fun areItemsTheSame(oldItem: ProfileDesignResponse,
          newItem: ProfileDesignResponse) = oldItem.id == newItem.id

      override fun areContentsTheSame(oldItem: ProfileDesignResponse,
          newItem: ProfileDesignResponse) = oldItem == newItem
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProfileDesignViewHolder(
      LayoutInflater.from(parent.context).inflate(R.layout.layout_profile_design_image, parent,
          false))

  override fun onBindViewHolder(holder: ProfileDesignViewHolder, position: Int) {
    holder.bind(getItem(position))
  }

  inner class ProfileDesignViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val binding = LayoutProfileDesignImageBinding.bind(itemView)
    private val context = itemView.context

    fun bind(data: ProfileDesignResponse) {
      with(binding) {
        ImageLoader.loadImageUrl(context, data.image, imageViewProfileDesign)
        root.setOnClickListener {
          onClickListener.invoke(data.id)
        }
      }
    }
  }
}