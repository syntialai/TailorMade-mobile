package com.future.tailormade_design_detail.feature.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.future.tailormade.base.view.BaseFragment
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade_design_detail.databinding.FragmentDesignDetailBinding
import com.future.tailormade_design_detail.feature.viewModel.DesignDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DesignDetailFragment : BaseFragment() {

  companion object {
    fun newInstance() = DesignDetailFragment()
  }

  private val viewModel: DesignDetailViewModel by viewModels()

  private lateinit var binding: FragmentDesignDetailBinding

  override fun getLogName(): String = "com.future.tailormade_design_detail.feature.view.DesignDetailFragment"

  override fun getViewModel(): BaseViewModel = viewModel

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View {
    binding = FragmentDesignDetailBinding.inflate(inflater, container, false)
    return binding.root
  }
}