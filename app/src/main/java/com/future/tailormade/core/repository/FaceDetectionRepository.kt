package com.future.tailormade.core.repository

import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face

interface FaceDetectionRepository {

  fun detectInImage(inputImage: InputImage): Task<List<Face>>
}