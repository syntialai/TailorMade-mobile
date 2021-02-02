package com.future.tailormade_profile.feature.settings.view

import android.os.Bundle
import android.view.View
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.future.tailormade.base.repository.AuthSharedPrefRepository
import com.future.tailormade.base.view.BaseActivity
import com.future.tailormade.util.view.ToastHelper
import com.future.tailormade_profile.R
import com.future.tailormade_profile.databinding.ActivitySettingsBinding
import com.future.tailormade_router.actions.Action
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsActivity : BaseActivity() {

  private lateinit var binding: ActivitySettingsBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivitySettingsBinding.inflate(layoutInflater)
    toolbar = binding.topToolbarSettings
    setContentView(binding.root)
    setupToolbar(getString(R.string.settings_label))

    if (savedInstanceState == null) {
      supportFragmentManager.beginTransaction().replace(binding.layoutSettings.id,
          SettingsFragment()).commit()
    }
  }

  @AndroidEntryPoint
  class SettingsFragment : PreferenceFragmentCompat() {

    @Inject
    lateinit var authSharedPrefRepository: AuthSharedPrefRepository

    override fun onCreatePreferences(savedInstanceState: Bundle?,
        rootKey: String?) {
      setPreferencesFromResource(R.xml.settings_preferences, rootKey)
    }

    override fun onPreferenceTreeClick(preference: Preference?): Boolean {
      val key = preference?.key
      return when(key.orEmpty()) {
        getString(R.string.pref_title_sign_out_settings) -> {
          signOut()
          true
        }
        getString(R.string.pref_title_about_settings) -> {
          showToast()
          true
        }
        else -> false
      }
    }

    private fun signOut() {
      context?.let { context ->
        authSharedPrefRepository.clearSharedPrefs()
        Action.goToSignIn(context)
        activity?.finishAndRemoveTask()
      }
    }

    private fun showToast() {
      activity?.findViewById<View>(android.R.id.content)?.let {
        ToastHelper.showToast(it, getString(R.string.about_message), true)
      }
    }
  }
}