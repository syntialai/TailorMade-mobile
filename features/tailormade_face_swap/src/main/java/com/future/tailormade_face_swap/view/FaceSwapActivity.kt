package com.future.tailormade_face_swap.view

import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.os.Bundle
import com.future.tailormade.config.Constants
import com.future.tailormade.feature.base.view.BaseSplitActivity
import com.future.tailormade.util.extension.orZero
import com.future.tailormade.util.view.ToastHelper
import com.future.tailormade_face_swap.R
import com.future.tailormade_face_swap.databinding.ActivityFaceSwapBinding
import com.future.tailormade_face_swap.exception.FaceSwapException
import com.future.tailormade_face_swap.util.BitmapHelper
import com.future.tailormade_face_swap.util.FaceSwap
import com.future.tailormade_face_swap.util.FileHelper
import java.io.File

class FaceSwapActivity : BaseSplitActivity() {

  companion object {
    private const val PARAM_SWAP_FACE_IMAGE_SOURCE = "PARAM_SWAP_FACE_IMAGE_SOURCE"
    private const val PARAM_SWAP_FACE_IMAGE_DESTINATION = "PARAM_SWAP_FACE_IMAGE_DESTINATION"
  }

  private lateinit var binding: ActivityFaceSwapBinding

  private var bitmapDestination: Bitmap? = null
  private var bitmapSource: Bitmap? = null

  override fun getScreenName() = "Face Swap"

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityFaceSwapBinding.inflate(layoutInflater)
    toolbar = binding.topToolbarFaceSwap
    setContentView(binding.root)
    setupToolbar(getScreenName())
    getImage()

    bitmapDestination?.let { bitmapDestination ->
      bitmapSource?.let { bitmapSource ->
        startSwapFace(bitmapDestination, bitmapSource)
      }
    }
  }

  private fun getImage() {
    intent?.getStringExtra(PARAM_SWAP_FACE_IMAGE_SOURCE)?.let {
      bitmapSource = BitmapHelper.convertFilePathToBitmap(it)
    }
    intent?.getStringExtra(PARAM_SWAP_FACE_IMAGE_DESTINATION)?.let {
      bitmapDestination = BitmapHelper.convertUrlToBitmap(this@FaceSwapActivity, it)
    }
  }

  private fun startSwapFace(bitmapDestination: Bitmap, bitmapSource: Bitmap) {
    val adjustedBitmap = BitmapHelper.getAdjustedBitmapSize(bitmapDestination, bitmapSource)
    val finalBitmapDestination = adjustedBitmap.first
    val finalBitmapSource = adjustedBitmap.second

    showToast(getString(R.string.swapping_face))

    launchCoroutineOnIO({
      val faceSwap = FaceSwap(finalBitmapDestination, finalBitmapSource)

      try {
        faceSwap.prepareSelfieSwapping()
      } catch (e: FaceSwapException) {
        e.localizedMessage?.let { showErrorToast(it) }
        e.printStackTrace()
        return@launchCoroutineOnIO
      }

      swapFace(faceSwap)
    }, Constants.DEBOUNCE_TIME_500)
  }

  private fun swapFace(faceSwap: FaceSwap) {
    val swappedBitmap = faceSwap.selfieSwap()
    val swappedFace = Bitmap.createBitmap(swappedBitmap, 0, 0, bitmapDestination?.width.orZero(),
        bitmapDestination?.height.orZero())
    setImage(swappedFace)
  }

  private fun setImage(image: Bitmap) {
    with(binding) {
      imageViewFaceSwap.setImageBitmap(image)
      buttonDownloadSwapFace.setOnClickListener {
        downloadImage(image)
      }
      buttonShareSwapFace.setOnClickListener {
        shareImage(image)
      }
    }
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
    MediaScannerConnection.scanFile(
        this, arrayOf(imageFile.toString()), arrayOf(imageFile.name)) { _, _ ->
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