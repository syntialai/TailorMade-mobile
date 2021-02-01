package com.future.tailormade.tailor_app.feature.splash.view

import android.os.Bundle
import android.os.Handler
import androidx.activity.viewModels
import com.future.tailormade.base.view.BaseActivity
import com.future.tailormade.config.Constants
import com.future.tailormade.feature.splash.viewModel.SplashScreenViewModel
import com.future.tailormade.tailor_app.databinding.ActivitySplashScreenBinding
import com.future.tailormade_router.actions.Action
import com.future.tailormade_router.actions.TailorAction
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
    TailorAction.goToMain(this)
    this.finish()
  }

  private fun goToSignIn() {
    Action.goToSignIn(this)
    this.finish()
  }

  private fun setupObserver() {
    viewModel.isTokenExpired.observe(this, {
      Handler().postDelayed({
        if (it) {
          goToSignIn()
        } else {
          goToMain()
        }
      }, Constants.REFRESH_DELAY_TIME)
    })
  }
}