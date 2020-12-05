package com.future.tailormade.feature.splash.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import com.future.tailormade.base.view.BaseActivity
import com.future.tailormade.databinding.ActivitySplashScreenBinding
import com.future.tailormade.feature.main.view.MainActivity
import com.future.tailormade_router.actions.Action

class SplashScreenActivity : BaseActivity() {

  companion object {
    private const val DELAY_TIME = 2000L
  }

  private lateinit var binding: ActivitySplashScreenBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivitySplashScreenBinding.inflate(layoutInflater)
    setContentView(binding.root)

    setupSplashScreenTimer()
  }

  private fun goToMain() {
    startActivity(Intent(this, MainActivity::class.java))
  }

  private fun goToSignIn() {
    Action.goToSignIn(this)
  }

  private fun setupSplashScreenTimer() {
    Handler().postDelayed({
      validateUserAuth()
      this.finish()
    }, DELAY_TIME)
  }

  private fun validateUserAuth() {
    // TODO: Check token here
  }
}