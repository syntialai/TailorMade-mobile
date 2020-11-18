package com.future.tailormade_profile.feature.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.future.tailormade.base.view.BaseActivity
import com.future.tailormade_profile.R

class SettingsActivity : BaseActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_settings)
    if (savedInstanceState == null) {
      supportFragmentManager.beginTransaction().replace(R.id.settings,
          SettingsFragment()).commit()
    }
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
  }

  class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?,
        rootKey: String?) {
      setPreferencesFromResource(R.xml.settings_preferences, rootKey)
    }
  }
}