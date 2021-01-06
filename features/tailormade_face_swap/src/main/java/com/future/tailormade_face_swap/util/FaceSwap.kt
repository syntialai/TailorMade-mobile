package com.future.tailormade_face_swap.util

import android.graphics.Bitmap
import android.graphics.Point
import com.future.tailormade.util.extension.orEmptyList
import com.future.tailormade.util.extension.orZero
import com.future.tailormade_face_swap.exception.FaceSwapException
import org.opencv.android.OpenCVLoader
import org.opencv.android.Utils.bitmapToMat
import org.opencv.android.Utils.matToBitmap
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc

class FaceSwap(private val bitmapDestination: Bitmap, private val bitmapSource: Bitmap) {

  private var landmarksDestination: ArrayList<ArrayList<Point>>? = null
  private var landmarksSource: ArrayList<ArrayList<Point>>? = null
  private var landmarks: ArrayList<ArrayList<Point>>? = null
  private var bitmap: Bitmap? = null

  init {
    if (OpenCVLoader.initDebug()) {
      System.loadLibrary("nativefaceswap");
    }
  }

  private external fun portraitSwapNative(addrImg1: Long, addrImg2: Long, landmarksX1: IntArray?,
      landmarksY1: IntArray?, landmarksX2: IntArray?, landmarksY2: IntArray?, addrResult: Long)

  @Throws(FaceSwapException::class)
  fun prepareSelfieSwapping() {
    val landmarkDetector = FacialLandmarkDetector()
    landmarksDestination = landmarkDetector.detectPeopleAndLandmarks(bitmapDestination)
    landmarksSource = landmarkDetector.detectPeopleAndLandmarks(bitmapSource)

    if (landmarksDestination?.size.orZero() <= 1) throw FaceSwapException("Face(s) missing")
    if (landmarksSource?.size.orZero() <= 1) throw FaceSwapException("Face(s) missing")
  }

  @Throws(FaceSwapException::class)
  fun prepareManyFacedSwapping() {
    val landmarkDetector = FacialLandmarkDetector()
    bitmap?.let {
      landmarks = landmarkDetector.detectPeopleAndLandmarks(it)
      if (landmarks?.size.orZero() <= 1) throw FaceSwapException("Face(s) missing")
    }
  }

  fun selfieSwap(): Bitmap {
    val pointsDestination: ArrayList<Point> = landmarksDestination?.get(0).orEmptyList()
    val pointsSource: ArrayList<Point> = landmarksSource?.get(1).orEmptyList()
    return swap(bitmapDestination, bitmapSource, pointsDestination, pointsSource)
  }

  private fun swap(bitmapDestination: Bitmap, bitmapSource: Bitmap,
      pointsDestination: ArrayList<Point>, pointsSource: ArrayList<Point>): Bitmap {
    var X1: IntArray
    var Y1: IntArray
    var X2: IntArray
    var Y2: IntArray

    mapToSimpleIntArray(pointsDestination).apply {
      X1 = first
      Y1 = second
    }
    mapToSimpleIntArray(pointsSource).apply {
      X2 = first
      Y2 = second
    }

    val imageDestination = Mat()
    val imageSource = Mat()
    bitmapToMat(bitmapDestination, imageDestination)
    bitmapToMat(bitmapSource, imageSource)
    Imgproc.cvtColor(imageDestination, imageDestination, Imgproc.COLOR_BGRA2BGR)
    Imgproc.cvtColor(imageSource, imageSource, Imgproc.COLOR_BGRA2BGR)

    val swapped = Mat()
    portraitSwapNative(imageDestination.nativeObjAddr, imageSource.nativeObjAddr, X1, Y1, X2, Y2,
        swapped.nativeObjAddr)
    val bitmapSwapped = Bitmap.createBitmap(bitmapDestination.width, bitmapDestination.height,
        Bitmap.Config.ARGB_8888)
    matToBitmap(swapped, bitmapSwapped)
    return bitmapSwapped
  }

  private fun mapToSimpleIntArray(points: ArrayList<Point>): Pair<IntArray, IntArray> {
    val pointsX = IntArray(points.size)
    val pointsY = IntArray(points.size)

    for (index in points.indices) {
      pointsX[index] = points[index].x
      pointsY[index] = points[index].y
    }

    return Pair(pointsX, pointsY)
  }
}