package com.future.tailormade_face_swap.util

import android.graphics.Bitmap
import android.graphics.Point
import android.graphics.Rect
import com.future.tailormade.util.extension.orZero
import com.tzutalin.dlib.PedestrianDet
import kotlin.math.roundToInt

class FacialLandmarkDetector() {

  companion object {
    private const val MAX_SIZE = 1024
    private const val NO_RESIZE = 1.0
  }

  private var _bitmap: Bitmap? = null
  private val _landmarks: ArrayList<ArrayList<Point>> = arrayListOf()
  private val pedestrianDet = PedestrianDet()

  fun getLandmarks() = _landmarks

  fun getNumberOfFaces() = _landmarks.size

  fun detectPeopleAndLandmarks() {
    _bitmap?.let { bitmap ->
      val persons = pedestrianDet.detect(bitmap)
      val resizeRatio = getBitmapResizeRatio(bitmap)

      persons?.forEach { ret ->
        val bounds = Rect()
        bounds.left = (ret.left * resizeRatio).toInt()
        bounds.top = (ret.top * resizeRatio).toInt()
        bounds.right = (ret.right * resizeRatio).toInt()
        bounds.bottom = (ret.bottom * resizeRatio).toInt()

        val tempPoints: ArrayList<Point> = arrayListOf()

        ret.faceLandmarks?.let { landmarks ->
          landmarks.forEach { point ->
            val pointX = (point.x * resizeRatio).toInt()
            val pointY = (point.y * resizeRatio).toInt()
            tempPoints.add(Point(pointX, pointY))
          }
        }

        _landmarks.add(tempPoints)
      }
    }
  }

  fun setBitmap(bitmap: Bitmap) {
    this._bitmap = bitmap
    this._landmarks.clear()
  }

  private fun getBitmapResizeRatio(bitmap: Bitmap): Double {
    val bitmapRatio = bitmap.width / bitmap.height.toDouble()

    val newWidth = MAX_SIZE
    val newHeight = (newWidth / bitmapRatio).roundToInt()

    if (isBitmapSizeTooLarge(bitmap.width, bitmap.height)) {
      _bitmap = getResizedBitmap(bitmap, newWidth, newHeight)
      return _bitmap?.width?.toDouble().orZero() / bitmap.width
    }

    return NO_RESIZE
  }

  private fun getResizedBitmap(bitmap: Bitmap, newWidth: Int, newHeight: Int) = Bitmap.createScaledBitmap(
      bitmap, newWidth, newHeight, true)

  private fun isBitmapSizeTooLarge(width: Int, height: Int) = width > MAX_SIZE || height > MAX_SIZE
}