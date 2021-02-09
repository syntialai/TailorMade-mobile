package com.future.tailormade_search.feature.search.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.future.tailormade.util.extension.remove
import com.future.tailormade.util.image.ImageLoader
import com.future.tailormade_dls.databinding.LayoutCardProfileBinding
import com.future.tailormade_search.R
import com.future.tailormade_search.core.model.response.SearchTailorResponse

class SearchTailorListAdapter(private val onClickListener: (String, String) -> Unit) :
    ListAdapter<SearchTailorResponse, SearchTailorListAdapter.SearchTailorListViewHolder>(
        diffCallback) {

  companion object {
    private val diffCallback = object : DiffUtil.ItemCallback<SearchTailorResponse>() {
      override fun areItemsTheSame(oldItem: SearchTailorResponse, newItem: SearchTailorResponse): Boolean {
        return oldItem.id == newItem.id
      }

      override fun areContentsTheSame(oldItem: SearchTailorResponse, newItem: SearchTailorResponse): Boolean {
        return oldItem == newItem
      }
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SearchTailorListViewHolder(
      LayoutInflater.from(parent.context).inflate(R.layout.layout_card_profile, parent, false))

  override fun onBindViewHolder(holder: SearchTailorListViewHolder, position: Int) {
    holder.bind(getItem(position))
  }

  inner class SearchTailorListViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val context = view.context
    private val binding = LayoutCardProfileBinding.bind(view)

    fun bind(data: SearchTailorResponse) {
      with(binding) {
        textViewProfileName.text = data.name

        data.location?.address?.let {
          textViewProfileCity.text = it
        } ?: run {
          textViewProfileCity.remove()
        }

        ImageLoader.loadImageUrlWithFitCenterAndPlaceholder(context, data.imagePath.orEmpty(),
            R.drawable.illustration_dashboard_tailor_profile, imageViewProfile, true)

        root.setOnClickListener {
          onClickListener.invoke(data.id, data.name)
        }
      }
    }
  }
}