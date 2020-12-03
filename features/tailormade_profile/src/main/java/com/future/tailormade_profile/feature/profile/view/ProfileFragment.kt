package com.future.tailormade_profile.feature.profile.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.future.tailormade.base.view.BaseFragment
import com.future.tailormade_profile.databinding.FragmentProfileBinding
import com.future.tailormade_profile.feature.profile.viewModel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment() {

  companion object {
    fun newInstance() = ProfileFragment()
  }

  private val viewModel: ProfileViewModel by viewModels()

  private lateinit var binding: FragmentProfileBinding

  override fun getLogName() = "com.future.tailormade_profile.feature.profile.view.ProfileFragment"

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View {
    binding = FragmentProfileBinding.inflate(inflater, container, false)
    return binding.root
  }
}