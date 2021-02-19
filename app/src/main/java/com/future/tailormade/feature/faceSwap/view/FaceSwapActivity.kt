package com.future.tailormade.feature.faceSwap.view

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.drawable.Drawable
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.future.tailormade.R
import com.future.tailormade.base.view.BaseActivity
import com.future.tailormade.config.Constants
import com.future.tailormade.databinding.ActivityFaceSwapBinding
import com.future.tailormade.feature.faceSwap.model.FaceLandmarkPosition
import com.future.tailormade.feature.faceSwap.util.BitmapHelper
import com.future.tailormade.feature.faceSwap.util.FileHelper
import com.future.tailormade.feature.faceSwap.viewModel.FaceSwapViewModel
import com.future.tailormade.util.extension.orZero
import com.future.tailormade.util.view.ToastHelper
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@RequiresApi(Build.VERSION_CODES.N)
@AndroidEntryPoint
class FaceSwapActivity : BaseActivity() {

  companion object {
    private const val PARAM_SWAP_FACE_IMAGE_SOURCE = "PARAM_SWAP_FACE_IMAGE_SOURCE"
    private const val PARAM_SWAP_FACE_IMAGE_DESTINATION = "PARAM_SWAP_FACE_IMAGE_DESTINATION"
    private const val TYPE_SOURCE = 0
    private const val TYPE_DESTINATION = 1
  }

  private lateinit var binding: ActivityFaceSwapBinding

  private val viewModel: FaceSwapViewModel by viewModels()

  override fun getScreenName() = "Face Swap"

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityFaceSwapBinding.inflate(layoutInflater)
    toolbar = binding.topToolbarFaceSwap
    setContentView(binding.root)
    setupToolbar(getScreenName())
    getImage()
    setupObserver()
    showSkeleton(binding.imageViewFaceSwap, R.layout.layout_face_swap_image_skeleton)
  }

  private fun getImage() {
    intent?.getStringExtra(PARAM_SWAP_FACE_IMAGE_SOURCE)?.let { imageSource ->
      BitmapHelper.convertFileToBitmap(contentResolver, Uri.parse(imageSource))?.let {
        viewModel.setBitmapSource(it)
      }
    }
    intent?.getStringExtra(PARAM_SWAP_FACE_IMAGE_DESTINATION)?.let {
      launchCoroutineOnIO({
        BitmapHelper.getUrlToBitmapRequestBuilder(this@FaceSwapActivity, it).into(object :
            CustomTarget<Bitmap>() {
          override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
            viewModel.setBitmapDestination(resource)
            appLogger.logOnEvent("Bitmap destination: $resource")
          }

          override fun onLoadCleared(placeholder: Drawable?) {
            // No implementation needed
          }
        })
      })
    }
  }

  private fun setupObserver() {
    viewModel.bitmapSource.observe(this, {
      it?.let { bitmapSource ->
        appLogger.logOnMethod("bitmapSource observer")
        detectFaceLandmark(bitmapSource, TYPE_SOURCE)
      }
    })
    viewModel.bitmapDestination.observe(this, {
      it?.let { bitmapDestination ->
        appLogger.logOnMethod("bitmapDestination observer")
        detectFaceLandmark(bitmapDestination, TYPE_DESTINATION)
      }
    })

    viewModel.faceLandmarksSource.observe(this, {
      it?.let { faceSource ->
        appLogger.logOnMethod("faceLandmarksSource observer")
        viewModel.faceLandmarksDestination.value?.let { faceDestination ->
          validateNotNull(faceSource, faceDestination)
        }
      }
    })
    viewModel.faceLandmarksDestination.observe(this, {
      it?.let { faceDestination ->
        appLogger.logOnMethod("faceLandmarksDestination observer")
        viewModel.faceLandmarksSource.value?.let { faceSource ->
          validateNotNull(faceSource, faceDestination)
        }
      }
    })
  }

  private fun validateNotNull(faceSource: FaceLandmarkPosition, faceDestination: FaceLandmarkPosition) {
    viewModel.bitmapSource.value?.let { bitmapSource ->
      viewModel.bitmapDestination.value?.let { bitmapDestination ->
        deepFakeFace(faceDestination, bitmapDestination, faceSource, bitmapSource)
      }
    }
  }

  private fun detectFaceLandmark(bitmap: Bitmap, type: Int) {
    viewModel.detectImage(bitmap).addOnSuccessListener { facesDetected ->
      if (facesDetected.isNotEmpty()) {
        if (type == TYPE_SOURCE) {
          viewModel.setFaceLandmarkSource(facesDetected[0])
        } else {
          viewModel.setFaceLandmarkDestination(facesDetected[0])
        }
      } else {
        showErrorToast(getString(R.string.detect_image_face_message, getStringByType(type)))
      }
    }.addOnFailureListener {
      appLogger.logOnError(it.message.toString(), it)
      showErrorToast(getString(R.string.error_detecting_image_failed_message, getStringByType(type)))
    }
  }

  private fun getStringByType(type: Int): String {
    return if (type == TYPE_SOURCE) {
      "your image"
    } else {
      "design's image"
    }
  }

  private fun deepFakeFace(faceDestination: FaceLandmarkPosition, bitmapDestination: Bitmap,
      faceSource: FaceLandmarkPosition, bitmapSource: Bitmap) {
    val resultBitmap = Bitmap.createBitmap(bitmapDestination.width, bitmapDestination.height,
        bitmapDestination.config)
    val canvas = Canvas(resultBitmap)
    val paintDestination = Paint()

    faceDestination.left?.let { left ->
      faceDestination.right?.let {
        appLogger.logOnMethod("deepFakeFace")
        val croppedBitmapSource = getCroppedBitmapSource(faceSource, bitmapSource)

        canvas.drawBitmap(
            Bitmap.createScaledBitmap(croppedBitmapSource, faceDestination.width, faceDestination.height,
                true), left, faceDestination.top, null)

        paintDestination.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_ATOP)
        canvas.drawBitmap(bitmapDestination, 0f, 0f, paintDestination)

        setImage(resultBitmap)
      }
    }
  }

  private fun getCroppedBitmapSource(
      faceSource: FaceLandmarkPosition, bitmapSource: Bitmap): Bitmap = Bitmap.createBitmap(
      bitmapSource, faceSource.left?.toInt().orZero(), faceSource.top.toInt(), faceSource.width,
      faceSource.height)

  private fun setImage(image: Bitmap) {
    appLogger.logOnMethod("setImage")
    with(binding) {
      imageViewFaceSwap.setImageBitmap(image)
      buttonDownloadSwapFace.setOnClickListener {
        downloadImage(image)
      }
      buttonShareSwapFace.setOnClickListener {
        shareImage(image)
      }
    }
    hideSkeleton()
  }

  private fun downloadImage(image: Bitmap) {
    val imageFile = FileHelper.downloadImageFile(image)
    scanFile(imageFile)
  }

  private fun shareImage(image: Bitmap) {
    FileHelper.getImageUrlContentValues(contentResolver)?.let { uri ->
      BitmapHelper.compressImage(contentResolver, image, uri)

      val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = Constants.TYPE_IMAGE_ALL
        putExtra(Intent.EXTRA_STREAM, uri)
      }
      startActivity(Intent.createChooser(shareIntent, getString(R.string.share_image)))
    }
  }

  private fun scanFile(imageFile: File) {
    MediaScannerConnection.scanFile(this, arrayOf(imageFile.toString()), arrayOf(imageFile.name)) { _, _ ->
      showToast(getString(R.string.download_completed))
    }
  }

  private fun showToast(message: String) {
    ToastHelper.showToast(binding.root, message)
  }

  private fun showErrorToast(message: String) {
    ToastHelper.showErrorToast(this, binding.root, message)
  }
}