package com.future.tailormade_search.feature.filter.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.future.tailormade_dls.databinding.LayoutCheckableTextBinding
import com.future.tailormade_search.R

class ChooseListAdapter(private val list: List<Pair<Any, Boolean>>,
    private val onClickListener: (Int) -> Unit) :
    RecyclerView.Adapter<ChooseListAdapter.ChooseListViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ChooseListViewHolder(
      LayoutInflater.from(parent.context).inflate(
          R.layout.layout_checkable_text, parent, false))

  override fun onBindViewHolder(holder: ChooseListViewHolder, position: Int) {
    holder.bind(list[position], position)
  }

  override fun getItemCount(): Int = list.size

  inner class ChooseListViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = LayoutCheckableTextBinding.bind(view)

    fun bind(item: Pair<Any, Boolean>, position: Int) {
      with(binding) {
        textViewTextContent.text = item.first.toString()
        cardCheckableText.isChecked = item.second
        root.setOnClickListener {
          cardCheckableText.isChecked = !cardCheckableText.isChecked
          onClickListener.invoke(position)
        }
      }
    }
  }
}