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

class FaceSwap(private val bitmap1: Bitmap, private val bitmap2: Bitmap) {

  private var landmarks1: ArrayList<ArrayList<Point>>? = null
  private var landmarks2: ArrayList<ArrayList<Point>>? = null
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
    landmarks1 = landmarkDetector.detectPeopleAndLandmarks(bitmap1)
    landmarks2 = landmarkDetector.detectPeopleAndLandmarks(bitmap2)

    if (landmarks1?.size.orZero() <= 1) throw FaceSwapException("Face(s) missing")
    if (landmarks2?.size.orZero() <= 1) throw FaceSwapException("Face(s) missing")
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
    val pts1: ArrayList<Point> = landmarks1?.get(0).orEmptyList()
    val pts2: ArrayList<Point> = landmarks2?.get(1).orEmptyList()
    return swap(bitmap1, bitmap2, pts1, pts2)
  }

  private fun swap(bmp1: Bitmap, bmp2: Bitmap, points1: ArrayList<Point>, points2: ArrayList<Point>): Bitmap {
    var X1: IntArray
    var Y1: IntArray
    var X2: IntArray
    var Y2: IntArray

    mapToSimpleIntArray(points1).apply {
      X1 = first
      Y1 = second
    }
    mapToSimpleIntArray(points2).apply {
      X2 = first
      Y2 = second
    }

    val img1 = Mat()
    bitmapToMat(bmp1, img1)
    val img2 = Mat()
    bitmapToMat(bmp2, img2)
    Imgproc.cvtColor(img1, img1, Imgproc.COLOR_BGRA2BGR)
    Imgproc.cvtColor(img2, img2, Imgproc.COLOR_BGRA2BGR)
    val swapped = Mat()
    portraitSwapNative(img1.nativeObjAddr, img2.nativeObjAddr, X1, Y1, X2, Y2,
        swapped.nativeObjAddr)
    val bmp = Bitmap.createBitmap(bmp1.width, bmp1.height, Bitmap.Config.ARGB_8888)
    matToBitmap(swapped, bmp)
    return bmp
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