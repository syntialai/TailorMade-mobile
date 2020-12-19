package com.future.tailormade.feature.history.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.future.tailormade.R
import com.future.tailormade.config.Constants
import com.future.tailormade.core.model.response.history.OrderDesignResponse
import com.future.tailormade.core.model.response.history.OrderResponse
import com.future.tailormade.databinding.LayoutHistoryCardItemBinding
import com.future.tailormade.util.extension.toDateString
import com.future.tailormade.util.image.ImageLoader

class HistoryCardItemAdapter(private val onCardClickListener: (String) -> Unit) :
		ListAdapter<OrderResponse, HistoryCardItemAdapter.HistoryCardItemViewHolder>(diffCallback) {

	companion object {
		private val diffCallback = object : DiffUtil.ItemCallback<OrderResponse>() {

			override fun areItemsTheSame(oldItem: OrderResponse, newItem: OrderResponse): Boolean = oldItem.id == newItem.id

			override fun areContentsTheSame(oldItem: OrderResponse, newItem: OrderResponse): Boolean = oldItem == newItem
		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = HistoryCardItemViewHolder(
			LayoutInflater.from(parent.context).inflate(R.layout.layout_history_card_item, parent, true))

	override fun onBindViewHolder(holder: HistoryCardItemViewHolder, position: Int) {
		holder.bind(getItem(position))
	}

	inner class HistoryCardItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

		private val binding = LayoutHistoryCardItemBinding.bind(itemView)
		private val context = itemView.context

		fun bind(data: OrderResponse) {
			with(binding) {
				textViewHistoryOrderId.text = data.id
				textViewHistoryOrderDate.text = data.createdAt.toDateString(Constants.DD_MMM_YY)

				textViewHistoryDesignOrderedTitle.text = data.design.title
				textViewHistoryDesignOrderedColor.text = data.design.color
				textViewHistoryDesignOrderedSize.text = data.design.size

				ImageLoader.loadImageUrl(context, data.design.image, imageViewHistoryDesignOrdered)
				root.setOnClickListener {
					onCardClickListener.invoke(data.id)
				}
			}
			bindPriceData(data.design)
			bindLayoutTotalData(data)
		}

		private fun bindLayoutTotalData(data: OrderResponse) {
			// TODO: Convert with .toIndonesianCurrencyFormat() extension
			with(binding.layoutHistoryOrderedTotal) {
				textViewOrderQuantity.text = data.quantity.toString()
				textViewOrderTotalPrice.text = data.totalPrice.toString()
				textViewOrderTotalDiscount.text = data.totalDiscount.toString()
			}
		}

		private fun bindPriceData(design: OrderDesignResponse) {
			with(binding) {
				if (design.discount > 0.0) {
					// TODO: Convert with .toIndonesianCurrencyFormat() extension
					textViewHistoryDesignOrderedPriceBeforeDiscount.text = design.price.toString()
					textViewHistoryDesignOrderedPriceAfterDiscount.text =
							(design.price - design.discount).toString()
				}	else {
					// TODO: Convert with .toIndonesianCurrencyFormat() extension
					textViewHistoryDesignOrderedPrice.text = design.price.toString()
				}
			}
		}
	}
}