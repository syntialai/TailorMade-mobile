package com.future.tailormade_search.feature.search.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.future.tailormade.util.image.ImageLoader
import com.future.tailormade_dls.databinding.LayoutCardDesignBinding
import com.future.tailormade_search.R
import com.future.tailormade_search.core.model.response.SearchDesignResponse

class SearchDesignGridAdapter(private val onClickListener: (String) -> Unit) :
    ListAdapter<SearchDesignResponse, SearchDesignGridAdapter.SearchDesignGridViewHolder>(
        diffCallback) {

  companion object {
    private val diffCallback = object : DiffUtil.ItemCallback<SearchDesignResponse>() {
      override fun areItemsTheSame(oldItem: SearchDesignResponse, newItem: SearchDesignResponse): Boolean {
        return oldItem.id == newItem.id
      }

      override fun areContentsTheSame(oldItem: SearchDesignResponse, newItem: SearchDesignResponse): Boolean {
        return oldItem == newItem
      }
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SearchDesignGridViewHolder(
      LayoutInflater.from(parent.context).inflate(R.layout.layout_card_design, parent, false))

  override fun onBindViewHolder(holder: SearchDesignGridViewHolder, position: Int) {
    holder.bind(getItem(position))
  }

  inner class SearchDesignGridViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = LayoutCardDesignBinding.bind(view)
    private val context = view.context

    fun bind(data: SearchDesignResponse) {
      with(binding) {
        textViewDesignName.text = data.title
        textViewDesignPrice.text = data.price.toString()

        ImageLoader.loadImageUrl(context, data.imagePath, imageViewDesign)

        root.setOnClickListener {
          onClickListener.invoke(data.id)
        }
      }
    }
  }
}