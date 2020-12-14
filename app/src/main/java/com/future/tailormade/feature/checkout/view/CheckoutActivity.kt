package com.future.tailormade.feature.checkout.view

import android.os.Bundle
import com.future.tailormade.base.view.BaseActivity
import com.future.tailormade.databinding.ActivityCheckoutBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CheckoutActivity : BaseActivity() {

	private lateinit var binding: ActivityCheckoutBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityCheckoutBinding.inflate(layoutInflater)
		toolbar = binding.topToolbarCheckout
		setContentView(binding.root)
	}
}