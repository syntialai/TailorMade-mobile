package com.future.tailormade_profile.feature.editProfile.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.future.tailormade.base.view.BaseFragment
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade_profile.databinding.FragmentEditProfileSetLocationBinding
import com.future.tailormade_profile.feature.editProfile.viewModel.EditProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

@AndroidEntryPoint
class EditProfileSetLocationFragment : BaseFragment() {

  companion object {
    fun newInstance() = EditProfileSetLocationFragment()
  }

  private val viewModel: EditProfileViewModel by viewModels()

  private lateinit var binding: FragmentEditProfileSetLocationBinding

  override fun getLogName(): String =
      "com.future.tailormade_profile.feature.editProfile.view.EditProfileSetLocationFragment"

  override fun getScreenName(): String = "Set Location"

  override fun getViewModel(): BaseViewModel = viewModel

  override fun onNavigationIconClicked() {
    findNavController().navigateUp()
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View {
    binding = FragmentEditProfileSetLocationBinding.inflate(inflater, container,
        false)

    with(binding) {
      mapViewEditProfile.setTileSource(TileSourceFactory.MAPNIK)
      mapViewEditProfile.setBuiltInZoomControls(true)
      mapViewEditProfile.setMultiTouchControls(true)
    }

    getMyLocation()

    return binding.root
  }

  override fun onResume() {
    super.onResume()
    binding.mapViewEditProfile.onResume()
  }

  override fun onPause() {
    super.onPause()
    binding.mapViewEditProfile.onPause()
  }

  private fun getMyLocation() {
    context?.let { context ->
      val myLocationOverlay = MyLocationNewOverlay(
          GpsMyLocationProvider(context), binding.mapViewEditProfile).apply {
        enableMyLocation()
      }
      binding.mapViewEditProfile.overlays.add(myLocationOverlay)
    }
  }
}