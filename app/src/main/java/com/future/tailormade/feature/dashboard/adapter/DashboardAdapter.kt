package com.future.tailormade.feature.dashboard.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.future.tailormade.R
import com.future.tailormade.core.model.response.dashboard.DashboardTailorResponse
import com.future.tailormade.databinding.LayoutDashboardTailorBinding
import com.future.tailormade.util.extension.show

class DashboardAdapter :
    ListAdapter<DashboardTailorResponse, DashboardAdapter.DashboardViewHolder>(
        diffCallback) {

  companion object {
    private val diffCallback = object :
        DiffUtil.ItemCallback<DashboardTailorResponse>() {

      override fun areItemsTheSame(oldItem: DashboardTailorResponse,
          newItem: DashboardTailorResponse): Boolean = oldItem.id == newItem.id

      override fun areContentsTheSame(oldItem: DashboardTailorResponse,
          newItem: DashboardTailorResponse): Boolean = oldItem == newItem
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DashboardViewHolder(
      LayoutInflater.from(parent.context).inflate(
          R.layout.layout_dashboard_tailor, parent, true))

  override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
    holder.bind(getItem(position))
  }

  inner class DashboardViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = LayoutDashboardTailorBinding.bind(view)

    fun bind(data: DashboardTailorResponse) {
      with(binding) {
        // TODO: update layout data
      }
      data.designs?.let { designs ->
        showPreview()
        // TODO: bind designs
      }
    }

    private fun showPreview() {
      binding.viewSeparator.show()
      binding.textViewPreviewLabel.show()
    }
  }
}