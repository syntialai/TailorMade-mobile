package com.future.tailormade.feature.faceSwap.viewModel

import android.graphics.Bitmap
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.util.extension.orZero

class FaceSwapViewModel @ViewModelInject constructor() : BaseViewModel() {

  override fun getLogName() = "com.future.tailormade.feature.faceSwap.viewModel.FaceSwapViewModel"

  private var _bitmapSource = MutableLiveData<Bitmap>()
  val bitmapSource: LiveData<Bitmap>
    get() = _bitmapSource

  private var _bitmapDestination = MutableLiveData<Bitmap>()
  val bitmapDestination: LiveData<Bitmap>
    get() = _bitmapDestination

  fun setBitmapSource(bitmapSource: Bitmap) {
    _bitmapSource.value = bitmapSource
    appLogger.logOnMethod("setBitmapSource")
  }

  fun setBitmapDestination(bitmapDestination: Bitmap) {
    _bitmapDestination.value = bitmapDestination
    appLogger.logOnMethod("setBitmapDestination")
  }

  fun getSwappedFace(swappedBitmap: Bitmap): Bitmap = Bitmap.createBitmap(swappedBitmap, 0, 0,
      _bitmapDestination.value?.width.orZero(), _bitmapDestination.value?.height.orZero())
}