package com.future.tailormade.tailor_app.feature.order.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.future.tailormade.base.view.BaseFragment
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.tailor_app.databinding.FragmentIncomingOrderBinding
import com.future.tailormade.tailor_app.feature.order.adapter.IncomingOrderAdapter
import com.future.tailormade.tailor_app.feature.order.viewModel.IncomingOrderViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IncomingOrderFragment : BaseFragment() {

  companion object {
    fun newInstance() = IncomingOrderFragment()
  }

  private lateinit var binding: FragmentIncomingOrderBinding

  private val incomingOrderAdapter by lazy {
    IncomingOrderAdapter(this::onAcceptOrder, this::onRejectOrder)
  }
  private val viewModel: IncomingOrderViewModel by viewModels()

  override fun getLogName() =
      "com.future.tailormade.tailor_app.feature.order.view.IncomingOrderFragment"

  override fun getViewModel(): BaseViewModel = viewModel

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View {
    binding = FragmentIncomingOrderBinding.inflate(inflater, container, false)
    setupRecyclerView()
    return binding.root
  }

  private fun onAcceptOrder(id: String) {
    // TODO: Call viewmodel to accept order
  }

  private fun onRejectOrder(id: String) {
    // TODO: Call viewmodel to reject order
  }

  private fun setupRecyclerView() {
    with(binding.recyclerViewIncomingOrder) {
      layoutManager = LinearLayoutManager(context)
      adapter = incomingOrderAdapter
    }
  }
}