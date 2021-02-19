package com.future.tailormade.feature.faceSwap.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Point
import com.future.tailormade.feature.faceSwap.model.FaceSwapException
import com.future.tailormade.util.extension.orZero
import com.future.tailormade.util.logger.AppLogger
import com.getkeepsafe.relinker.ReLinker
import org.opencv.android.OpenCVLoader
import org.opencv.android.Utils.bitmapToMat
import org.opencv.android.Utils.matToBitmap
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc

class FaceSwap(context: Context, private val bitmapDestination: Bitmap,
    private val bitmapSource: Bitmap) {

  private var landmarksDestination: ArrayList<ArrayList<Point>>? = null
  private var landmarksSource: ArrayList<ArrayList<Point>>? = null
  private var landmarks: ArrayList<ArrayList<Point>>? = null
  private var bitmap: Bitmap? = null

//  companion object {
  private val logger = AppLogger("com.future.tailormade.feature.faceSwap.util.FaceSwap")
  private val reLinkerLogger = ReLinker.Logger {
    logger.logOnEvent(it)
  }

  init {
    if (OpenCVLoader.initDebug()) {
      //      OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_4_0, context, mLoaderCallback)
      ReLinker.log(reLinkerLogger).loadLibrary(context, "c++_shared")
      //    } else {
      //      mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS)
      //    }
    }
  }

  private external fun portraitSwapNative(addressImageDestination: Long, addressImageSource: Long,
      landmarksDestinationX: IntArray?, landmarksDestinationY: IntArray?,
      landmarksSourceX: IntArray?, landmarksSourceY: IntArray?, addressImageResult: Long)

  @Throws(FaceSwapException::class)
  fun prepareSelfieSwapping() {
    val landmarkDetector = FacialLandmarkDetector()
    landmarksDestination = landmarkDetector.detectPeopleAndLandmarks(bitmapDestination)
    landmarksSource = landmarkDetector.detectPeopleAndLandmarks(bitmapSource)

    throwErrorIfEmpty(landmarksDestination, "Face on the design's image is missing")
    throwErrorIfEmpty(landmarksSource, "Face on your image is missing")
  }

  @Throws(FaceSwapException::class)
  fun prepareManyFacedSwapping() {
    val landmarkDetector = FacialLandmarkDetector()
    bitmap?.let {
      landmarks = landmarkDetector.detectPeopleAndLandmarks(it)
      throwErrorIfEmpty(landmarks, "Face(s) missing")
    }
  }

  fun selfieSwap(): Bitmap? {
    landmarksDestination?.get(0)?.let { pointsDestination ->
      landmarksSource?.get(1)?.let { pointsSource ->
        return swap(bitmapDestination, bitmapSource, pointsDestination, pointsSource)
      }
    }
    return null
  }

  private fun swap(bitmapDestination: Bitmap, bitmapSource: Bitmap,
      pointsDestination: ArrayList<Point>, pointsSource: ArrayList<Point>): Bitmap {
    var bitmapDestinationX: IntArray
    var bitmapDestinationY: IntArray
    var bitmapSourceX: IntArray
    var bitmapSourceY: IntArray

    mapToSimpleIntArray(pointsDestination).apply {
      bitmapDestinationX = first
      bitmapDestinationY = second
    }
    mapToSimpleIntArray(pointsSource).apply {
      bitmapSourceX = first
      bitmapSourceY = second
    }

    val imageDestination = Mat()
    val imageSource = Mat()
    bitmapToMat(bitmapDestination, imageDestination)
    bitmapToMat(bitmapSource, imageSource)
    Imgproc.cvtColor(imageDestination, imageDestination, Imgproc.COLOR_BGRA2BGR)
    Imgproc.cvtColor(imageSource, imageSource, Imgproc.COLOR_BGRA2BGR)

    val swapped = Mat()
    portraitSwapNative(imageDestination.nativeObjAddr, imageSource.nativeObjAddr,
        bitmapDestinationX, bitmapDestinationY, bitmapSourceX, bitmapSourceY, swapped.nativeObjAddr)
    val bitmapSwapped = Bitmap.createBitmap(bitmapDestination.width, bitmapDestination.height,
        Bitmap.Config.ARGB_8888)
    matToBitmap(swapped, bitmapSwapped)
    return bitmapSwapped
  }

  private fun throwErrorIfEmpty(landmarksArray: ArrayList<ArrayList<Point>>?, message: String) {
    if (landmarksArray?.size.orZero() <= 1) throw FaceSwapException(message)
  }

  fun mapToSimpleIntArray(points: ArrayList<Point>): Pair<IntArray, IntArray> {
    val pointsX = IntArray(points.size)
    val pointsY = IntArray(points.size)

    for (index in points.indices) {
      pointsX[index] = points[index].x
      pointsY[index] = points[index].y
    }

    return Pair(pointsX, pointsY)
  }
}