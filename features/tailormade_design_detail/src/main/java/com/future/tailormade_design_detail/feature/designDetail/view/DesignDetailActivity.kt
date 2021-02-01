package com.future.tailormade_design_detail.feature.designDetail.view

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.future.tailormade.base.view.BaseActivity
import com.future.tailormade_design_detail.R
import com.future.tailormade_design_detail.databinding.ActivityDesignDetailBinding
import com.future.tailormade_design_detail.feature.addOrEditDesign.view.AddOrEditDesignFragmentDirections
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DesignDetailActivity : BaseActivity() {

  companion object {
    private const val PARAM_DESIGN_DETAIL_ID = "PARAM_DESIGN_DETAIL_ID"
    private const val PARAM_DESIGN_DETAIL_ADD = "PARAM_DESIGN_DETAIL_ADD"
  }

  private lateinit var binding: ActivityDesignDetailBinding
  private lateinit var navController: NavController

  var designDetailId: String = ""

  override fun getScreenName(): String = "Design Detail Page"

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityDesignDetailBinding.inflate(layoutInflater)
    toolbar = binding.topToolbarDesignDetail
    setContentView(binding.root)
    setupNavController()

    designDetailId = intent?.getStringExtra(PARAM_DESIGN_DETAIL_ID).orEmpty()
    goToAddOrEditDesign()
  }

  private fun goToAddOrEditDesign() {
    intent?.getBooleanExtra(PARAM_DESIGN_DETAIL_ADD, false).let {
      it?.let { add ->
        if (add) {
          navController.popBackStack()
          navController.navigate(
              AddOrEditDesignFragmentDirections.actionGlobalAddOrEditDesignFragment(null))
        }
      }
    }
  }

  private fun setupNavController() {
    val hostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_design_detail_fragment) as NavHostFragment
    navController = hostFragment.navController
  }
}