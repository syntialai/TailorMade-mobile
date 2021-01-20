package com.future.tailormade.feature.cart.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.future.tailormade.R
import com.future.tailormade.core.model.ui.cart.CartDesignUiModel
import com.future.tailormade.core.model.ui.cart.CartUiModel
import com.future.tailormade.databinding.LayoutCartItemBinding
import com.future.tailormade.util.extension.remove
import com.future.tailormade.util.extension.show
import com.future.tailormade.util.image.ImageLoader

class CartAdapter(private val deleteCartItemListener: (String, String) -> Unit,
    private val checkoutCartItemListener: (String) -> Unit,
    private val editItemQuantityListener: (String, Int) -> Unit,
    private val goToDesignDetail: (String) -> Unit) :
    ListAdapter<CartUiModel, CartAdapter.CartViewHolder>(diffCallback) {

  companion object {
    private val diffCallback = object : DiffUtil.ItemCallback<CartUiModel>() {

      override fun areItemsTheSame(oldItem: CartUiModel, newItem: CartUiModel): Boolean =
          oldItem.id == newItem.id && oldItem.design.id == newItem.design.id

      override fun areContentsTheSame(oldItem: CartUiModel, newItem: CartUiModel): Boolean = oldItem == newItem
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CartViewHolder(
      LayoutInflater.from(parent.context).inflate(R.layout.layout_cart_item, parent, false))

  override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
    holder.bind(getItem(position), position)
  }

  inner class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = LayoutCartItemBinding.bind(view)
    private val context = view.context

    fun bind(data: CartUiModel, position: Int) {
      bindDesignData(data.design)
      with(binding) {
        root.id = position
        with(spinButtonNumber) {
          setValue(data.quantity)
          setAddSpinNumberButtonListener {
            editItemQuantityListener.invoke(data.id, it)
          }
          setReduceSpinNumberButtonListener {
            editItemQuantityListener.invoke(data.id, it)
          }
        }

        buttonDeleteOrder.setOnClickListener {
          deleteCartItemListener.invoke(data.id, data.design.title)
        }
        buttonCheckoutOrder.setOnClickListener {
          checkoutCartItemListener.invoke(data.id)
        }
      }
    }

    private fun bindDesignData(design: CartDesignUiModel) {
      with(binding) {
        groupCartButton.show()

        textViewOrderTitle.text = design.title
        textViewOrderSize.text = design.size
        textViewOrderColor.text = design.color

        design.discount?.let {
          showDiscount()
          textViewOrderAfterDiscount.text = design.discount
          textViewOrderBeforeDiscount.text = design.price
        } ?: run {
          textViewOrderPrice.text = design.price
        }

        ImageLoader.loadImageUrl(context, design.image, imageViewOrder)

        root.setOnClickListener {
          goToDesignDetail.invoke(design.id)
        }
      }
    }

    private fun showDiscount() {
      with(binding){
        groupDiscountTextView.show()
        textViewOrderPrice.remove()
      }
    }
  }
}