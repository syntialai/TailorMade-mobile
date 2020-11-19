package com.future.tailormade.feature

import android.os.Bundle
import com.future.tailormade.base.view.BaseActivity
import com.future.tailormade.databinding.ActivityMainBinding
import com.future.tailormade_router.actions.Action
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startActivity(Action.goToProfile(this))
    }
}