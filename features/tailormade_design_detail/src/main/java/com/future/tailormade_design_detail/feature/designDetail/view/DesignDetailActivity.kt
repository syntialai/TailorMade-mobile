package com.future.tailormade_design_detail.feature.designDetail.view

import android.os.Bundle
import androidx.navigation.findNavController
import com.future.tailormade.base.view.BaseActivity
import com.future.tailormade_design_detail.R
import com.future.tailormade_design_detail.databinding.ActivityDesignDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DesignDetailActivity : BaseActivity() {

  companion object {
    private const val PARAM_DESIGN_DETAIL_ID = "PARAM_DESIGN_DETAIL_ID"
  }

  private lateinit var binding: ActivityDesignDetailBinding

  var designDetailId: String = ""

  override fun getScreenName(): String = "Design Detail Page"

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityDesignDetailBinding.inflate(layoutInflater)
    toolbar = binding.topToolbarDesignDetail
    setContentView(binding.root)
    setSupportActionBar(toolbar)

    designDetailId = intent?.getStringExtra(PARAM_DESIGN_DETAIL_ID).orEmpty()
    if (designDetailId.isBlank()) {
      findNavController(R.id.nav_design_detail_graph).navigate(
          R.id.action_global_addOrEditDesignFragment)
    }
  }
}