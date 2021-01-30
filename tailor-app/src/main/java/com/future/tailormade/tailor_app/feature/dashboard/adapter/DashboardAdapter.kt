package com.future.tailormade.tailor_app.feature.dashboard.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.future.tailormade.tailor_app.R
import com.future.tailormade.tailor_app.core.model.ui.dashboard.DashboardDesignUiModel
import com.future.tailormade.util.image.ImageLoader
import com.future.tailormade_dls.databinding.LayoutCardDesignBinding

class DashboardAdapter(private val onClickListener: (String) -> Unit,
    private val onLongClickListener: (String) -> Unit) :
    ListAdapter<DashboardDesignUiModel, DashboardAdapter.DashboardViewHolder>(diffCallback) {

  companion object {
    private val diffCallback = object : DiffUtil.ItemCallback<DashboardDesignUiModel>() {

      override fun areItemsTheSame(oldItem: DashboardDesignUiModel,
          newItem: DashboardDesignUiModel) = oldItem.id == newItem.id

      override fun areContentsTheSame(oldItem: DashboardDesignUiModel,
          newItem: DashboardDesignUiModel) = oldItem == newItem
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DashboardViewHolder(
      LayoutInflater.from(parent.context).inflate(R.layout.layout_card_design, parent, false))

  override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
    holder.bind(getItem(position))
  }

  inner class DashboardViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val binding = LayoutCardDesignBinding.bind(itemView)
    private val context = itemView.context

    fun bind(data: DashboardDesignUiModel) {
      with(binding) {
        textViewDesignName.text = data.title
        textViewDesignPrice.text = data.price

        ImageLoader.loadImageUrl(context, data.image, imageViewDesign)

        root.setOnClickListener {
          onClickListener.invoke(data.id)
        }

        root.setOnLongClickListener {
          root.isChecked = !root.isChecked
          onLongClickListener.invoke(data.id)
          true
        }
      }
    }
  }
}