package com.future.tailormade.feature.faceSwap.viewModel

import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.core.repository.FaceDetectionRepository
import com.future.tailormade.feature.faceSwap.model.FaceLandmarkPosition
import com.future.tailormade.util.extension.orZero
import com.google.android.gms.tasks.Task
import com.google.android.gms.vision.Frame
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceLandmark
import java.lang.Float.min

@RequiresApi(Build.VERSION_CODES.N)
class FaceSwapViewModel @ViewModelInject constructor(
    private val faceDetectionRepository: FaceDetectionRepository,
    @Assisted private val savedStateHandle: SavedStateHandle) : BaseViewModel() {

  override fun getLogName() = "com.future.tailormade.feature.faceSwap.viewModel.FaceSwapViewModel"

  private var _bitmapSource = MutableLiveData<Bitmap>()
  val bitmapSource: LiveData<Bitmap>
    get() = _bitmapSource

  private var _bitmapDestination = MutableLiveData<Bitmap>()
  val bitmapDestination: LiveData<Bitmap>
    get() = _bitmapDestination

  private var _faceLandmarksSource = MutableLiveData<FaceLandmarkPosition>()
  val faceLandmarksSource: LiveData<FaceLandmarkPosition>
    get() = _faceLandmarksSource

  private var _faceLandmarksDestination = MutableLiveData<FaceLandmarkPosition>()
  val faceLandmarksDestination: LiveData<FaceLandmarkPosition>
    get() = _faceLandmarksDestination

  fun detectImage(imageBitmap: Bitmap): Task<List<Face>> {
    val inputImage = InputImage.fromBitmap(imageBitmap, Frame.ROTATION_0)
    return faceDetectionRepository.detectInImage(inputImage)
  }

  fun setBitmapSource(bitmapSource: Bitmap) {
    _bitmapSource.value = bitmapSource
    appLogger.logOnMethod("setBitmapSource")
  }

  fun setBitmapDestination(bitmapDestination: Bitmap) {
    _bitmapDestination.value = bitmapDestination
    appLogger.logOnMethod("setBitmapDestination")
  }

  fun setFaceLandmarkDestination(faceLandmark: Face) {
    _faceLandmarksDestination.value = createFaceLandmarkPosition(faceLandmark)
    appLogger.logOnMethod("setFaceLandmarkDestination: ${_faceLandmarksDestination.value}")
  }

  fun setFaceLandmarkSource(faceLandmark: Face) {
    _faceLandmarksSource.value = createFaceLandmarkPosition(faceLandmark)
    appLogger.logOnMethod("setFaceLandmarkSource: ${_faceLandmarksSource.value}")
  }

  private fun createFaceLandmarkPosition(face: Face) = FaceLandmarkPosition(
      top = getTopFacePosition(face), left = face.getLandmark(FaceLandmark.LEFT_EAR)?.position?.x,
      right = face.getLandmark(FaceLandmark.RIGHT_EAR)?.position?.x,
      bottom = getBottomFacePosition(face), rotation = face.headEulerAngleZ,
      minusRotation = 360f - face.headEulerAngleZ).apply {
    width = (right.orZero() - left.orZero()).toInt()
    height = (bottom - top).toInt()
  }

  private fun getTopFacePosition(face: Face) = getAverage(face.boundingBox.top.toFloat(),
      min(face.getLandmark(FaceLandmark.LEFT_EYE)?.position?.y.orZero(),
          face.getLandmark(FaceLandmark.RIGHT_EYE)?.position?.y.orZero()))

  private fun getBottomFacePosition(face: Face) = getAverage(face.boundingBox.bottom.toFloat(),
      face.getLandmark(FaceLandmark.MOUTH_BOTTOM)?.position?.y.orZero())

  private fun getAverage(firstValue: Float, secondValue: Float) = (firstValue + secondValue) / 2f
}