package com.future.tailormade_face_swap.view

import android.os.Bundle
import com.future.tailormade.base.view.BaseActivity
import com.future.tailormade_face_swap.databinding.ActivityFaceSwapBinding

class FaceSwapActivity : BaseActivity() {

  private lateinit var binding: ActivityFaceSwapBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityFaceSwapBinding.inflate(layoutInflater)
    setContentView(binding.root)
  }
}