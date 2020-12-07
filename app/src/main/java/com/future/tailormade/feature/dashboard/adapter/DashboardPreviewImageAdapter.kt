package com.future.tailormade.feature.dashboard.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.future.tailormade.R
import com.future.tailormade.core.model.response.dashboard.DashboardDesignResponse
import com.future.tailormade.databinding.LayoutDashboardPreviewImageBinding
import com.future.tailormade.util.image.ImageLoader

class DashboardPreviewImageAdapter :
    ListAdapter<DashboardDesignResponse, DashboardPreviewImageAdapter.DashboardPreviewImageViewHolder>(
        diffCallback) {

  companion object {
    private val diffCallback = object :
        DiffUtil.ItemCallback<DashboardDesignResponse>() {

      override fun areItemsTheSame(oldItem: DashboardDesignResponse,
          newItem: DashboardDesignResponse): Boolean = oldItem.id == newItem.id

      override fun areContentsTheSame(oldItem: DashboardDesignResponse,
          newItem: DashboardDesignResponse): Boolean = oldItem == newItem
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DashboardPreviewImageViewHolder(
      LayoutInflater.from(parent.context).inflate(
          R.layout.layout_dashboard_preview_image, parent, true))

  override fun onBindViewHolder(
      holderPreviewImage: DashboardPreviewImageViewHolder, position: Int) {
    holderPreviewImage.bind(getItem(position))
  }

  inner class DashboardPreviewImageViewHolder(view: View) :
      RecyclerView.ViewHolder(view) {

    private val binding = LayoutDashboardPreviewImageBinding.bind(view)
    private val context = view.context

    fun bind(data: DashboardDesignResponse) {
      ImageLoader.loadImageUrl(context, data.image,
          binding.imageViewPreviewDesign)
    }
  }
}