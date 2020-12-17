package com.future.tailormade.feature.dashboard.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.future.tailormade.R
import com.future.tailormade.core.model.response.dashboard.DashboardTailorResponse
import com.future.tailormade.databinding.LayoutDashboardTailorBinding
import com.future.tailormade.util.extension.show
import com.future.tailormade.util.image.ImageLoader

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
    private val context = view.context
    private val previewImageAdapter by lazy {
      DashboardPreviewImageAdapter()
    }

    fun bind(data: DashboardTailorResponse) {
      with(binding) {
        layoutCardTailor.textViewProfileName.text = data.name
        (data.location?.city.orEmpty() + data.location?.country?.let {
          (", $it")
        }.orEmpty()).also {
          layoutCardTailor.textViewProfileCity.text = it
        }

        data.image?.let {
          ImageLoader.loadImageUrl(context, it,
              layoutCardTailor.imageViewProfile)
        }

        data.designs?.let { designs ->
          showPreview()
          setupPreviewImageAdapter()
          previewImageAdapter.submitList(designs)
        }
      }
    }

    private fun setupPreviewImageAdapter() {
      binding.recyclerViewPreviewDesigns.apply {
        layoutManager = LinearLayoutManager(context,
            LinearLayoutManager.HORIZONTAL, false)
        adapter = previewImageAdapter
      }
    }

    private fun showPreview() {
      binding.viewSeparator.show()
      binding.textViewPreviewLabel.show()
    }
  }
}