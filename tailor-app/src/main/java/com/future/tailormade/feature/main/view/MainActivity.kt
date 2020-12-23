package com.future.tailormade.feature.main.view

import android.os.Bundle
import com.future.tailormade.base.view.BaseActivity
import com.future.tailormade.tailor_app.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {

  private lateinit var binding: ActivityMainBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)
  }
}