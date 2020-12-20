package com.future.tailormade.feature.checkout.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.future.tailormade.R
import com.future.tailormade.core.model.ui.checkout.EditMeasurementFieldUiModel
import com.future.tailormade.databinding.LayoutEditMeasurementFieldItemBinding
import com.future.tailormade.util.image.ImageLoader

class EditMeasurementFieldAdapter :
    ListAdapter<EditMeasurementFieldUiModel, EditMeasurementFieldAdapter.EditMeasurementViewHolder>(
        diffCallback) {

  companion object {
    private val diffCallback = object : DiffUtil.ItemCallback<EditMeasurementFieldUiModel>() {

      override fun areItemsTheSame(oldItem: EditMeasurementFieldUiModel,
          newItem: EditMeasurementFieldUiModel) = oldItem.label == newItem.label

      override fun areContentsTheSame(oldItem: EditMeasurementFieldUiModel,
          newItem: EditMeasurementFieldUiModel) = oldItem == newItem
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = EditMeasurementViewHolder(
      LayoutInflater.from(parent.context).inflate(R.layout.layout_edit_measurement_field_item,
          parent, true))

  override fun onBindViewHolder(holder: EditMeasurementViewHolder, position: Int) {
    holder.bind(getItem(position))
  }

  inner class EditMeasurementViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val binding = LayoutEditMeasurementFieldItemBinding.bind(itemView)
    private val context = itemView.context

    fun bind(data: EditMeasurementFieldUiModel) {
      with(binding) {
        ImageLoader.loadImageResource(context, data.image, imageViewEditMeasurement)
        textViewEditMeasurementLabel.text = data.label
        editTextEditMeasurement.setText(data.value)
      }
    }
  }
}