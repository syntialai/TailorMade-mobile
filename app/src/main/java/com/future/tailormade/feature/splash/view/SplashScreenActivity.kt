package com.future.tailormade.feature.splash.view

import android.os.Bundle
import androidx.activity.viewModels
import com.future.tailormade.base.view.BaseActivity
import com.future.tailormade.databinding.ActivitySplashScreenBinding
import com.future.tailormade.feature.splash.viewModel.SplashScreenViewModel
import com.future.tailormade_router.actions.Action
import com.future.tailormade_router.actions.UserAction
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashScreenActivity : BaseActivity() {

  private lateinit var binding: ActivitySplashScreenBinding

  private val viewModel: SplashScreenViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivitySplashScreenBinding.inflate(layoutInflater)
    setContentView(binding.root)

    viewModel.validateToken()
    setupObserver()
  }

  private fun goToMain() {
    UserAction.goToMain(this)
  }

  private fun goToSignIn() {
    Action.goToSignIn(this)
  }

  private fun setupObserver() {
    viewModel.isTokenExpired.observe(this, {
      if (it) {
        goToSignIn()
      } else {
        goToMain()
      }
    })
  }
}