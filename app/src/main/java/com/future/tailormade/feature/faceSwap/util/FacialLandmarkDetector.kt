package com.future.tailormade.feature.faceSwap.util

import android.graphics.Bitmap
import android.graphics.Point
import android.graphics.Rect
import com.future.tailormade.util.extension.orZero
import com.tzutalin.dlib.Constants
import com.tzutalin.dlib.FaceDet
import kotlin.math.roundToInt

class FacialLandmarkDetector() {

  companion object {
    private const val MAX_SIZE = 1024
    private const val NO_RESIZE = 1.0
  }

  private val _landmarks: ArrayList<ArrayList<Point>> = arrayListOf()
  private val faceDet = FaceDet(Constants.getFaceShapeModelPath())

  fun getLandmarks() = _landmarks

  fun getNumberOfFaces() = _landmarks.size

  fun detectPeopleAndLandmarks(bitmap: Bitmap): ArrayList<ArrayList<Point>> {
    var mutableBitmap = bitmap.copy(bitmap.config, true)
    val faces = faceDet.detect(bitmap)
    val bitmapResizeRatio = getBitmapResizeRatio(bitmap)
    val resizeRatio = bitmapResizeRatio.second
    bitmapResizeRatio.first?.let {
      mutableBitmap = it
    }

    faces?.forEach { ret ->
      val bounds = Rect()
      bounds.left = (ret.left * resizeRatio).toInt()
      bounds.top = (ret.top * resizeRatio).toInt()
      bounds.right = (ret.right * resizeRatio).toInt()
      bounds.bottom = (ret.bottom * resizeRatio).toInt()

      val points: ArrayList<Point> = arrayListOf()

      ret.faceLandmarks?.let { landmarks ->
        landmarks.forEach { point ->
          val pointX = (point.x * resizeRatio).toInt()
          val pointY = (point.y * resizeRatio).toInt()
          points.add(Point(pointX, pointY))
        }
      }

      _landmarks.add(points)
    }
    return _landmarks
  }

  private fun getBitmapResizeRatio(bitmap: Bitmap): Pair<Bitmap?, Double> {
    val bitmapRatio = bitmap.width / bitmap.height.toDouble()

    val newWidth = MAX_SIZE
    val newHeight = (newWidth / bitmapRatio).roundToInt()

    if (isBitmapSizeTooLarge(bitmap.width, bitmap.height)) {
      val resizedBitmap = getResizedBitmap(bitmap, newWidth, newHeight)
      return Pair(resizedBitmap, resizedBitmap.width.toDouble().orZero() / bitmap.width)
    }

    return Pair(null, NO_RESIZE)
  }

  private fun getResizedBitmap(bitmap: Bitmap, newWidth: Int, newHeight: Int) = Bitmap.createScaledBitmap(
      bitmap, newWidth, newHeight, true)

  private fun isBitmapSizeTooLarge(width: Int, height: Int) = width > MAX_SIZE || height > MAX_SIZE
}