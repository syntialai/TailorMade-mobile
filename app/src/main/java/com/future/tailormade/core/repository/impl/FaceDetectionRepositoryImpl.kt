package com.future.tailormade.core.repository.impl

import com.future.tailormade.core.repository.FaceDetectionRepository
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import javax.inject.Inject

class FaceDetectionRepositoryImpl : FaceDetectionRepository {

  private val firebaseFaceDetector by lazy {
    FaceDetection.getClient(getCloudVisionOptionsBuilder())
  }

  override fun detectInImage(inputImage: InputImage) = firebaseFaceDetector.process(inputImage)

  private fun getCloudVisionOptionsBuilder() = FaceDetectorOptions.Builder()
      .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
      .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
      .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
      .setMinFaceSize(0.15f)
      .enableTracking()
      .build()
}