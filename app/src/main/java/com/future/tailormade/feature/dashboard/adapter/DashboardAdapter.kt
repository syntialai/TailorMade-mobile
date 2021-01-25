package com.future.tailormade.feature.dashboard.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.future.tailormade.R
import com.future.tailormade.core.model.ui.dashboard.DashboardTailorUiModel
import com.future.tailormade.databinding.LayoutDashboardTailorBinding
import com.future.tailormade.util.extension.show
import com.future.tailormade.util.image.ImageLoader

class DashboardAdapter(private val onClickListener: (String) -> Unit) :
    ListAdapter<DashboardTailorUiModel, DashboardAdapter.DashboardViewHolder>(diffCallback) {

  companion object {
    private val diffCallback = object :
        DiffUtil.ItemCallback<DashboardTailorUiModel>() {

      override fun areItemsTheSame(oldItem: DashboardTailorUiModel,
          newItem: DashboardTailorUiModel): Boolean = oldItem.id == newItem.id

      override fun areContentsTheSame(oldItem: DashboardTailorUiModel,
          newItem: DashboardTailorUiModel): Boolean = oldItem == newItem
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DashboardViewHolder(
      LayoutInflater.from(parent.context).inflate(R.layout.layout_dashboard_tailor, parent, false))

  override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
    holder.bind(getItem(position))
  }

  inner class DashboardViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = LayoutDashboardTailorBinding.bind(view)
    private val context = view.context
    private val previewImageAdapter by lazy {
      DashboardPreviewImageAdapter()
    }

    fun bind(data: DashboardTailorUiModel) {
      with(binding.layoutCardTailor) {
        textViewProfileName.text = data.name
        buttonChatTailor.show()

        data.location?.let {
          textViewProfileCity.text = it
        } ?: run {
          textViewProfileCity.remove()
        }

        ImageLoader.loadImageUrlWithFitCenterAndPlaceholder(context, data.image.orEmpty(),
            R.drawable.illustration_dashboard_tailor_profile, imageViewProfile)

        data.designs?.let { designs ->
          if (designs.isNotEmpty()) {
            showPreview()
            setupPreviewImageAdapter()
            previewImageAdapter.submitList(designs)
          }
        }
      }

      binding.root.setOnClickListener {
        onClickListener.invoke(data.id)
      }
    }

    private fun setupPreviewImageAdapter() {
      binding.recyclerViewPreviewDesigns.apply {
        layoutManager = LinearLayoutManager(context)
        adapter = previewImageAdapter
      }
    }

    private fun showPreview() {
      binding.viewSeparator.show()
      binding.textViewPreviewLabel.show()
    }
  }
}