package com.future.tailormade.feature.base.view

import android.content.Context
import com.future.tailormade.base.view.BaseActivity
import com.google.android.play.core.splitcompat.SplitCompat

abstract class BaseSplitActivity : BaseActivity() {

  override fun attachBaseContext(newBase: Context?) {
    super.attachBaseContext(newBase)
    SplitCompat.installActivity(this)
  }
}