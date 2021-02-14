package com.future.tailormade.feature.faceSwap.view

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.ethanhua.skeleton.SkeletonScreen
import com.future.tailormade.R
import com.future.tailormade.base.view.BaseActivity
import com.future.tailormade.config.Constants
import com.future.tailormade.databinding.ActivityFaceSwapBinding
import com.future.tailormade.feature.faceSwap.exception.FaceSwapException
import com.future.tailormade.feature.faceSwap.util.BitmapHelper
import com.future.tailormade.feature.faceSwap.util.FaceSwap
import com.future.tailormade.feature.faceSwap.util.FileHelper
import com.future.tailormade.feature.faceSwap.viewModel.FaceSwapViewModel
import com.future.tailormade.util.view.ToastHelper
import java.io.File

class FaceSwapActivity : BaseActivity() {

  companion object {
    private const val PARAM_SWAP_FACE_IMAGE_SOURCE = "PARAM_SWAP_FACE_IMAGE_SOURCE"
    private const val PARAM_SWAP_FACE_IMAGE_DESTINATION = "PARAM_SWAP_FACE_IMAGE_DESTINATION"
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
        viewModel.bitmapDestination.value?.let { bitmapDestination ->
          startSwapFace(bitmapDestination, bitmapSource)
        }
      }
    })
    viewModel.bitmapDestination.observe(this, {
      it?.let { bitmapDestination ->
        appLogger.logOnMethod("bitmapDestination observer")
        viewModel.bitmapSource.value?.let { bitmapSource ->
          startSwapFace(bitmapDestination, bitmapSource)
        }
      }
    })
  }

  private fun startSwapFace(bitmapDestination: Bitmap, bitmapSource: Bitmap) {
    val adjustedBitmap = BitmapHelper.getAdjustedBitmapSize(bitmapDestination, bitmapSource)
    val finalBitmapDestination = adjustedBitmap.first
    val finalBitmapSource = adjustedBitmap.second

    showToast(getString(R.string.swapping_face))

    launchCoroutineOnIO({
      val faceSwap = FaceSwap(this, finalBitmapDestination, finalBitmapSource)

      try {
        appLogger.logOnMethod("Face swap")
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
    faceSwap.selfieSwap()?.let { swappedBitmap ->
      val swappedFace = viewModel.getSwappedFace(swappedBitmap)
      setImage(swappedFace)
    }
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