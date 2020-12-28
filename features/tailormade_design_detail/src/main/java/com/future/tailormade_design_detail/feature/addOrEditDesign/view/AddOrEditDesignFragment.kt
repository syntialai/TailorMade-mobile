package com.future.tailormade_design_detail.feature.addOrEditDesign.view

import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.future.tailormade.base.view.BaseFragment
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.config.Constants
import com.future.tailormade.util.extension.remove
import com.future.tailormade.util.extension.show
import com.future.tailormade.util.image.ImageHelper
import com.future.tailormade_design_detail.R
import com.future.tailormade_design_detail.databinding.FragmentAddOrEditDesignBinding
import com.future.tailormade_design_detail.feature.addOrEditDesign.viewModel.AddOrEditDesignViewModel
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddOrEditDesignFragment : BaseFragment() {

  companion object {
    private const val GALLERY_REQUEST_CODE = 1

    fun newInstance() = AddOrEditDesignFragment()
  }

  private lateinit var binding: FragmentAddOrEditDesignBinding

  private val viewModel: AddOrEditDesignViewModel by viewModels()

  override fun getLogName() =
      "com.future.tailormade_design_detail.feature.addOrEditDesign.view.AddOrEditDesignFragment"

  override fun getScreenName() = "Add Design"

  override fun getViewModel(): BaseViewModel = viewModel

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View {
    binding = FragmentAddOrEditDesignBinding.inflate(inflater, container, false)
    with(binding) {
      layoutAddOrEditImage.buttonChangeDesignImage.setOnClickListener {
        openGallery()
      }
      buttonAddSize.setOnClickListener {
        openAddSizeBottomSheet()
      }
      buttonAddColor.setOnClickListener {
        openAddColorBottomSheet()
      }
      buttonSaveDesignInfo.setOnClickListener {
        // TODO: call view model to save design
      }
    }
    hideImagePreview()
    return binding.root
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (resultCode == Activity.RESULT_OK && requestCode == GALLERY_REQUEST_CODE) {
      data?.data?.let { imageUri ->
        // TODO: call viewmodel to save image uri
        if (isImagePreviewShown().not()) {
          activity?.contentResolver?.let {
            showImagePreview(imageUri, ImageHelper.getFileName(it, imageUri).orEmpty())
          }
        }
      }
    }
  }

  private fun addSizeChip(text: String) {
    val chipBinding = layoutInflater.inflate(R.layout.item_choose_size_chip,
        binding.chipGroupDesignSize, true) as Chip
    with(chipBinding) {
      this.text = text
      isCloseIconEnabled = true
      isCheckable = false
      setOnCloseIconClickListener {
        // TODO: Call viewmodel to remove size
        binding.chipGroupDesignSize.removeView(this)
      }
      setOnClickListener {
        openAddSizeBottomSheet(text)
      }
    }
    binding.chipGroupDesignSize.addView(chipBinding)
  }

  private fun addColorChip(text: String, color: String) {
    val chipBinding = layoutInflater.inflate(R.layout.item_choose_color_chip,
        binding.chipGroupDesignColor, false) as Chip
    with(chipBinding) {
      this.text = text
      this.chipIconTint = ColorStateList.valueOf(Color.parseColor(color))
      isCloseIconEnabled = true
      isCheckable = false
      setOnCloseIconClickListener {
        // TODO: Call viewmodel to remove color
        binding.chipGroupDesignColor.removeView(this)
      }
      setOnClickListener {
        openAddColorBottomSheet(text, color)
      }
    }
    binding.chipGroupDesignColor.addView(chipBinding)
  }

  private fun hideImagePreview() {
    with(binding.layoutAddOrEditImage) {
      groupFilledImageState.remove()
      groupEmptyImageState.show()
      root.setOnClickListener {
        openGallery()
      }
    }
    setClickable(true)
  }

  private fun isImagePreviewShown() = binding.layoutAddOrEditImage.groupFilledImageState.isShown

  private fun openGallery() {
    val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
      type = Constants.TYPE_IMAGE_ALL
    }
    this.startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE)
  }

  private fun openAddColorBottomSheet(name: String? = null, color: String? = null) {
    AddColorBottomSheetFragment.newInstance(::addColorChip, name, color).show(parentFragmentManager,
        getScreenName())
  }

  private fun openAddSizeBottomSheet(name: String? = null) {
    AddSizeBottomSheetFragment.newInstance(::addSizeChip, name).show(parentFragmentManager,
        getScreenName())
  }

  private fun setClickable(value: Boolean) {
    with(binding.layoutAddOrEditImage.root) {
      isClickable = value
      isFocusable = value
      isFocusableInTouchMode = value
    }
  }

  private fun showImagePreview(imageUri: Uri, imageName: String) {
    with(binding.layoutAddOrEditImage) {
      groupFilledImageState.show()
      groupEmptyImageState.remove()

      imageViewPreviewDesignImage.setImageURI(imageUri)
      textViewPreviewDesignImageName.text = imageName
    }
    setClickable(false)
  }
}