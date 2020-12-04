package com.future.tailormade_profile.feature.profile.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.future.tailormade.base.view.BaseFragment
import com.future.tailormade_profile.R
import com.future.tailormade_profile.databinding.FragmentProfileAboutBinding
import com.future.tailormade_profile.feature.profile.viewModel.ProfileViewModel

class ProfileAboutFragment : BaseFragment() {

  companion object {
    fun newInstance() = ProfileAboutFragment()
  }

  private val viewModel: ProfileViewModel by viewModels()

  private lateinit var binding: FragmentProfileAboutBinding

  override fun getLogName() = "com.future.tailormade_profile.feature.profile.view.ProfileAboutFragment"

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View {
    binding = FragmentProfileAboutBinding.inflate(inflater, container, false)

    with(binding) {
      textViewEditAbout.setOnClickListener {
        // TODO: Go to edit about
      }
    }

    return binding.root
  }
}