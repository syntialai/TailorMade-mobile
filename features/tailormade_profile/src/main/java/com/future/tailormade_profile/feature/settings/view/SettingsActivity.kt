package com.future.tailormade_profile.feature.settings.view

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.future.tailormade.base.view.BaseActivity
import com.future.tailormade_profile.R
import com.future.tailormade_profile.databinding.ActivitySettingsBinding

class SettingsActivity : BaseActivity() {

  private lateinit var binding: ActivitySettingsBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivitySettingsBinding.inflate(layoutInflater)
    toolbar = binding.topToolbarSettings
    setContentView(binding.root)
    setupToolbar(getString(R.string.settings_label))

    if (savedInstanceState == null) {
      supportFragmentManager.beginTransaction().replace(binding.flSettings.id,
          SettingsFragment()).commit()
    }
  }

  class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?,
        rootKey: String?) {
      setPreferencesFromResource(R.xml.settings_preferences, rootKey)
    }
  }
}