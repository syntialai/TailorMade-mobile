package com.future.tailormade_design_detail.feature.view

import android.os.Bundle
import com.future.tailormade.base.view.BaseActivity
import com.future.tailormade_design_detail.databinding.ActivityDesignDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DesignDetailActivity : BaseActivity() {

  companion object {
    private const val PARAM_DESIGN_DETAIL_ID = "DESIGN_DETAIL_ID"
  }

  private lateinit var binding: ActivityDesignDetailBinding

  private var _designDetailId: String = ""

  override fun getScreenName(): String = "Design Detail Page"

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityDesignDetailBinding.inflate(layoutInflater)
    toolbar = binding.topToolbarDesignDetail
    setContentView(binding.root)

    setDesignDetailId(intent?.getStringExtra(PARAM_DESIGN_DETAIL_ID).orEmpty())
  }

  fun getId() = _designDetailId

  private fun setDesignDetailId(id: String) {
    _designDetailId = id
  }
}