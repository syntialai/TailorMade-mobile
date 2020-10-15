package com.mta.blibli.tailormade_auth.feature.login.view

import android.os.Bundle
import com.future.tailormade.base.view.BaseActivity
import com.mta.blibli.tailormade_auth.databinding.ActivityLoginBinding

class LoginActivity : BaseActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}