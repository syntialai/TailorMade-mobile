package com.future.tailormade_search.feature.search.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.future.tailormade.util.image.ImageLoader
import com.future.tailormade_dls.databinding.LayoutCardDesignBinding
import com.future.tailormade_search.R
import com.future.tailormade_search.core.model.response.SearchDesignResponse

class SearchDesignListAdapter(
    private var designList: List<SearchDesignResponse>) :
    RecyclerView.Adapter<SearchDesignListAdapter.SearchDesignListViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SearchDesignListViewHolder(
      LayoutInflater.from(parent.context).inflate(R.layout.layout_card_design,
          parent, false))

  override fun onBindViewHolder(holder: SearchDesignListViewHolder,
      position: Int) {
    holder.bind(designList[position])
  }

  override fun getItemCount(): Int = designList.size

  inner class SearchDesignListViewHolder(view: View) :
      RecyclerView.ViewHolder(view) {

    private val context = view.context
    private val binding = LayoutCardDesignBinding.bind(view)

    fun bind(data: SearchDesignResponse) {
      with(binding) {
        textViewDesignName.text = data.title
        textViewDesignPrice.text = data.price.toString()

        ImageLoader.loadImageUrl(context, data.imagePath, imageViewDesign)

        root.setOnClickListener {
          // Go to design detail
        }
      }
    }
  }
}