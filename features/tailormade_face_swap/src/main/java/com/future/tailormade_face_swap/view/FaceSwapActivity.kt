package com.future.tailormade_face_swap.view

import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.os.Bundle
import com.future.tailormade.base.view.BaseActivity
import com.future.tailormade.config.Constants
import com.future.tailormade.util.extension.orZero
import com.future.tailormade.util.view.ToastHelper
import com.future.tailormade_face_swap.R
import com.future.tailormade_face_swap.databinding.ActivityFaceSwapBinding
import com.future.tailormade_face_swap.exception.FaceSwapException
import com.future.tailormade_face_swap.util.BitmapHelper
import com.future.tailormade_face_swap.util.FaceSwap
import com.future.tailormade_face_swap.util.FileHelper
import java.io.File

class FaceSwapActivity : BaseActivity() {

  companion object {
    private const val PARAM_SWAP_FACE_IMAGE = "PARAM_SWAP_FACE_IMAGE"
    private const val PARAM_SWAP_FACE_IMAGE_URL = "PARAM_SWAP_FACE_IMAGE_URL"
  }

  private lateinit var binding: ActivityFaceSwapBinding

  private var bitmapDesignImage: Bitmap? = null
  private var bitmapImage: Bitmap? = null

  override fun getScreenName() = "Face Swap"

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityFaceSwapBinding.inflate(layoutInflater)
    toolbar = binding.topToolbarFaceSwap
    setContentView(binding.root)
    setupToolbar(getScreenName())
    getImage()

    if (bitmapDesignImage != null && bitmapImage != null) {
      startSwapFace(bitmapDesignImage!!, bitmapImage!!)
    }
  }

  private fun getImage() {
    intent?.getByteArrayExtra(PARAM_SWAP_FACE_IMAGE)?.let {
      bitmapImage = BitmapHelper.convertByteArrayToBitmap(it)
    }
    intent?.getStringExtra(PARAM_SWAP_FACE_IMAGE_URL)?.let {
      bitmapDesignImage = BitmapHelper.convertUrlToBitmap(this@FaceSwapActivity, it)
    }
  }

  private fun startSwapFace(bitmap1: Bitmap, bitmap2: Bitmap) {
    val adjustedBitmap = BitmapHelper.getAdjustedBitmapSize(bitmap1, bitmap2)
    val finalBitmap1 = adjustedBitmap.first
    val finalBitmap2 = adjustedBitmap.second

    showToast(getString(R.string.swapping_face))

    launchCoroutineOnIO({
      val faceSwap = FaceSwap(finalBitmap1, finalBitmap2)

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
    val swappedFace = Bitmap.createBitmap(swappedBitmap, 0, 0, bitmapDesignImage?.width.orZero(),
        bitmapDesignImage?.height.orZero())
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