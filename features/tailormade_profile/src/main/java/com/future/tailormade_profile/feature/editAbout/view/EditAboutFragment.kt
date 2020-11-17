package com.future.tailormade_profile.feature.editAbout.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.future.tailormade.base.view.BaseFragment
import com.future.tailormade_profile.databinding.FragmentEditAboutBinding
import com.future.tailormade_profile.feature.editAbout.viewmodel.EditAboutViewModel

class EditAboutFragment : BaseFragment() {

  private val viewModel: EditAboutViewModel by viewModels()

  private lateinit var binding: FragmentEditAboutBinding

  override fun getLogName(): String =
      "com.future.tailormade_profile.feature.editAbout.view.EditAboutFragment"

  override fun getScreenName(): String = "Edit About"

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    binding = FragmentEditAboutBinding.inflate(inflater, container, false)
    return binding.root
  }

  companion object {

    @JvmStatic fun newInstance() = EditAboutFragment()
  }
}