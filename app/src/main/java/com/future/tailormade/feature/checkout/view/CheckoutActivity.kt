package com.future.tailormade.feature.checkout.view

import android.os.Bundle
import androidx.activity.viewModels
import com.future.tailormade.base.view.BaseActivity
import com.future.tailormade.databinding.ActivityCheckoutBinding
import com.future.tailormade.feature.checkout.viewModel.CheckoutViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CheckoutActivity : BaseActivity() {

  companion object {
    private const val PARAM_CART_ITEM_ID = "PARAM_CART_ITEM_ID"
  }

  private lateinit var binding: ActivityCheckoutBinding

  private val viewModel: CheckoutViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityCheckoutBinding.inflate(layoutInflater)
    toolbar = binding.topToolbarCheckout
    setContentView(binding.root)
    getCartItemId()
  }

  override fun onBackPressed() {
    onNavigationIconClicked?.invoke()
  }

  private fun getCartItemId() {
    intent?.getStringExtra(PARAM_CART_ITEM_ID)?.let {
      viewModel.setId(it)
    }
  }
}