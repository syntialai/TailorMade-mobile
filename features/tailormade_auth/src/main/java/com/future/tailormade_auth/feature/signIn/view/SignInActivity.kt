package com.future.tailormade_auth.feature.signIn.view

import android.os.Bundle
import com.future.tailormade.base.view.BaseActivity
import com.future.tailormade_auth.databinding.ActivitySignInBinding

class SignInActivity : BaseActivity() {

  private lateinit var binding: ActivitySignInBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivitySignInBinding.inflate(layoutInflater)
    setContentView(binding.root)
  }
}