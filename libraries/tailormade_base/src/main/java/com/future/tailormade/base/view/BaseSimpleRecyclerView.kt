package com.future.tailormade.base.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class BaseSimpleRecyclerView(private val layoutId: Int):
    RecyclerView.Adapter<BaseSimpleRecyclerView.BaseSimpleViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseSimpleViewHolder {
    return BaseSimpleViewHolder(
        LayoutInflater.from(parent.context).inflate(layoutId, parent, false))
  }

  override fun onBindViewHolder(holder: BaseSimpleViewHolder, position: Int) {
    // No Implementation Needed
  }

  override fun getItemCount(): Int = 10

  inner class BaseSimpleViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    // No Implementation Needed
  }
}