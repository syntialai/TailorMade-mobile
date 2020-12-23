package com.future.tailormade.feature.history.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.future.tailormade.base.view.BaseActivity
import com.future.tailormade.databinding.ActivityHistoryBinding

class HistoryActivity : BaseActivity() {

  private lateinit var binding: ActivityHistoryBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityHistoryBinding.inflate(layoutInflater)
    toolbar = binding.topToolbarHistory
    setContentView(binding.root)
  }
}