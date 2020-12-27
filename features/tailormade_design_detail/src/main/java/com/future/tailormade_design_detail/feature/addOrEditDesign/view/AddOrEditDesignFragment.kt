package com.future.tailormade_design_detail.feature.addOrEditDesign.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.future.tailormade.base.view.BaseFragment
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade_design_detail.R
import com.future.tailormade_design_detail.databinding.FragmentAddOrEditDesignBinding
import com.future.tailormade_design_detail.feature.addOrEditDesign.viewModel.AddOrEditDesignViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddOrEditDesignFragment : BaseFragment() {

  companion object {
    fun newInstance() = AddOrEditDesignFragment()
  }

  private lateinit var binding: FragmentAddOrEditDesignBinding

  private val viewModel: AddOrEditDesignViewModel by viewModels()

  override fun getLogName() =
      "com.future.tailormade_design_detail.feature.addOrEditDesign.view.AddOrEditDesignFragment"

  override fun getViewModel(): BaseViewModel = viewModel

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View {
    binding = FragmentAddOrEditDesignBinding.inflate(inflater, container, false)
    return binding.root
  }
}