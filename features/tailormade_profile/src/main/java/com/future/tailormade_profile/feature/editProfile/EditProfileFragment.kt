package com.future.tailormade_profile.feature.editProfile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.future.tailormade.base.view.BaseFragment
import com.future.tailormade_profile.databinding.EditProfileFragmentBinding

class EditProfileFragment : BaseFragment() {

    private lateinit var binding: EditProfileFragmentBinding

    private val viewModel: EditProfileViewModel by viewModels()

    override fun getScreenName(): String =
        "com.future.tailormade_profile.feature.editProfile.EditProfileFragment"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        binding = EditProfileFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance() = EditProfileFragment()
    }
}