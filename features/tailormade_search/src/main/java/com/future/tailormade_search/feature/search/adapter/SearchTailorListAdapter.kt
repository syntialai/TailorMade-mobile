package com.future.tailormade_search.feature.search.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.future.tailormade.util.image.ImageLoader
import com.future.tailormade_dls.databinding.LayoutCardProfileBinding
import com.future.tailormade_search.R
import com.future.tailormade_search.core.model.response.SearchTailorResponse

class SearchTailorListAdapter(
    private var tailorList: List<SearchTailorResponse>) :
    RecyclerView.Adapter<SearchTailorListAdapter.SearchTailorListViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SearchTailorListViewHolder(
      LayoutInflater.from(parent.context).inflate(R.layout.layout_card_profile,
          parent, false))

  override fun onBindViewHolder(holder: SearchTailorListViewHolder,
      position: Int) {
    holder.bind(tailorList[position])
  }

  override fun getItemCount(): Int = tailorList.size

  inner class SearchTailorListViewHolder(view: View) :
      RecyclerView.ViewHolder(view) {

    private val context = view.context
    private val binding = LayoutCardProfileBinding.bind(view)

    fun bind(data: SearchTailorResponse) {
      with(binding) {
        textViewProfileName.text = data.name
        textViewProfileLocation.text = data.location

        ImageLoader.loadImageUrl(context, data.imagePath, imageViewProfile)

        root.setOnClickListener {
          // Go to tailor profile
        }
      }
    }
  }
}