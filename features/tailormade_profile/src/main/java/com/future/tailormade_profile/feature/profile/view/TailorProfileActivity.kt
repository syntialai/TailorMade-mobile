package com.future.tailormade_profile.feature.profile.view

import android.os.Bundle
import androidx.activity.viewModels
import com.future.tailormade.base.repository.AuthSharedPrefRepository
import com.future.tailormade.base.view.BaseActivity
import com.future.tailormade.util.image.ImageLoader
import com.future.tailormade_profile.R
import com.future.tailormade_profile.core.model.ui.ProfileInfoUiModel
import com.future.tailormade_profile.databinding.ActivityTailorProfileBinding
import com.future.tailormade_profile.feature.profile.adapter.ProfilePagerAdapter
import com.future.tailormade_profile.feature.profile.viewModel.TailorProfileViewModel
import com.future.tailormade_router.actions.Action
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi

@AndroidEntryPoint
class TailorProfileActivity : BaseActivity() {

  companion object {
    private const val PARAM_TAILOR_ID = "PARAM_TAILOR_ID"
  }

  @Inject
  lateinit var authSharedPrefRepository: AuthSharedPrefRepository

  private lateinit var binding: ActivityTailorProfileBinding

  private val viewModel: TailorProfileViewModel by viewModels()

  override fun getScreenName(): String = "Tailor Profile"

  @ExperimentalCoroutinesApi
  @InternalCoroutinesApi
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityTailorProfileBinding.inflate(layoutInflater)
    toolbar = binding.topToolbarProfile
    setContentView(binding.root)
    setSupportActionBar(toolbar)
    setupOnNavigationIconClicked {
      finish()
    }
    setupToolbar(getTailorId() ?: getScreenName())
    setupViewPager()
    setupTabLayout()
    setupObserver()
  }

  @ExperimentalCoroutinesApi
  @InternalCoroutinesApi
  private fun setupObserver() {
    getTailorId()?.let {
      viewModel.setTailorId(it)
    }
    viewModel.fetchTailorProfileInfo()
    viewModel.profileInfoUiModel.observe(this, {
      it?.let { profileInfo ->
        setupProfileData(profileInfo)
      }
    })
  }

  private fun getTailorId() = intent.getStringExtra(PARAM_TAILOR_ID)

  private fun setupProfileData(data: ProfileInfoUiModel) {
    with(binding.layoutProfileInfo) {
      buttonChatTailor.setOnClickListener {
        Action.goToChatRoom(this@TailorProfileActivity,
            "${authSharedPrefRepository.userId}_${data.id}", data.name)
      }

      textViewProfileName.text = data.name
      textViewProfileCity.text = data.address

      ImageLoader.loadImageUrlWithFitCenterAndPlaceholder(this@TailorProfileActivity,
          data.image.orEmpty(),
          R.drawable.illustration_dashboard_tailor_profile, imageViewProfile)
    }
  }

  private fun setupTabLayout() {
    with(binding) {
      TabLayoutMediator(tabLayoutTailorProfile, viewPagerTailorProfile) { tab, position ->
        tab.text = when (position) {
          ProfilePagerAdapter.DESIGN_FRAGMENT_INDEX -> getString(R.string.design_label)
          ProfilePagerAdapter.ABOUT_FRAGMENT_INDEX -> getString(R.string.about_label)
          else -> ""
        }
      }.attach()
    }
  }

  private fun setupViewPager() {
    binding.viewPagerTailorProfile.adapter = ProfilePagerAdapter(supportFragmentManager, lifecycle)
  }
}