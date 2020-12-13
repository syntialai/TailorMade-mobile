package com.future.tailormade.feature.cart.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.future.tailormade.base.view.BaseFragment
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.databinding.FragmentCartBinding
import com.future.tailormade.feature.cart.viewModel.CartViewModel
import com.future.tailormade.util.extension.remove
import com.future.tailormade.util.extension.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartFragment : BaseFragment() {

	companion object {
		fun newInstance() = CartFragment()
	}

	private val viewModel: CartViewModel by viewModels()

	private lateinit var binding: FragmentCartBinding

	override fun getLogName(): String = "com.future.tailormade.feature.cart.view.CartFragment"

	override fun getScreenName(): String = "Cart"

	override fun getViewModel(): BaseViewModel = viewModel

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
			savedInstanceState: Bundle?): View {
		binding = FragmentCartBinding.inflate(inflater, container, false)
		setupRecyclerView()
		return binding.root
	}

	private fun hideState() {
		with(binding) {
			imageViewCartState.remove()
			textViewCartTitleState.remove()
			textViewCartDescriptionState.remove()
		}
	}

	private fun setupRecyclerView() {
		with(binding.recyclerViewCartList) {
			layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
			// TODO: Set adapter
		}
	}

	private fun showRecyclerView() {
		binding.recyclerViewCartList.show()
	}
}