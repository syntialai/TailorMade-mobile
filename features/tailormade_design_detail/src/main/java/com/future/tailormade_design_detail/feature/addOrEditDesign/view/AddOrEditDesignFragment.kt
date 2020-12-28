package com.future.tailormade_design_detail.feature.addOrEditDesign.view

import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.future.tailormade.base.view.BaseFragment
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.config.Constants
import com.future.tailormade.util.extension.remove
import com.future.tailormade.util.extension.show
import com.future.tailormade.util.image.ImageHelper
import com.future.tailormade.util.image.ImageLoader
import com.future.tailormade_design_detail.R
import com.future.tailormade_design_detail.core.mapper.DesignDetailMapper
import com.future.tailormade_design_detail.core.model.response.ColorResponse
import com.future.tailormade_design_detail.core.model.response.DesignDetailResponse
import com.future.tailormade_design_detail.core.model.response.SizeResponse
import com.future.tailormade_design_detail.core.model.ui.SizeDetailUiModel
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

  private val args: AddOrEditDesignFragmentArgs by navArgs()
  private val viewModel: AddOrEditDesignViewModel by viewModels()

  override fun getLogName() =
      "com.future.tailormade_design_detail.feature.addOrEditDesign.view.AddOrEditDesignFragment"

  override fun getScreenName() = args.designDetail?.let {
    "Edit Design"
  } ?: "Add Design"

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

  @RequiresApi(Build.VERSION_CODES.O)
  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (resultCode == Activity.RESULT_OK && requestCode == GALLERY_REQUEST_CODE) {
      data?.data?.let { imageUri ->
        imageUri.path?.let { path ->
          viewModel.setImage(path)
          if (isImagePreviewShown().not()) {
            activity?.contentResolver?.let {
              showImagePreview(imageUri, ImageHelper.getFileName(it, imageUri).orEmpty())
            }
          }
        }
      }
    }
  }

  override fun setupFragmentObserver() {
    super.setupFragmentObserver()

    args.designDetail?.let { designDetailResponse ->
      viewModel.setDesignDetailResponse(designDetailResponse)
    }
    viewModel.designDetailResponse.observe(viewLifecycleOwner, {
      setData(it)
    })
  }

  private fun addSizeChip(text: String, sizeDetail: SizeDetailUiModel) {
    val chipBinding = layoutInflater.inflate(R.layout.item_choose_size_chip,
        binding.chipGroupDesignSize, true) as Chip
    with(chipBinding) {
      this.text = text
      isCloseIconEnabled = true
      isCheckable = false
      setOnCloseIconClickListener {
        viewModel.removeSize(text, sizeDetail)
        binding.chipGroupDesignSize.removeView(this)
      }
      setOnClickListener {
        openAddSizeBottomSheet(text, sizeDetail)
      }
    }
    viewModel.addSize(text, sizeDetail)
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
        viewModel.removeColor(text, color)
        binding.chipGroupDesignColor.removeView(this)
      }
      setOnClickListener {
        openAddColorBottomSheet(text, color)
      }
    }
    viewModel.addColor(text, color)
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

  private fun openAddSizeBottomSheet(name: String? = null, sizeDetail: SizeDetailUiModel? = null) {
    AddSizeBottomSheetFragment.newInstance(::addSizeChip, name, sizeDetail).show(
        parentFragmentManager, getScreenName())
  }

  private fun setClickable(value: Boolean) {
    with(binding.layoutAddOrEditImage.root) {
      isClickable = value
      isFocusable = value
      isFocusableInTouchMode = value
    }
  }

  private fun setData(response: DesignDetailResponse) {
    with(binding) {
      editTextDesignName.setText(response.title)
      editTextDesignPrice.setText(response.price.toString())
      editTextDesignDiscount.setText(response.discount.toString())
      editTextDesignDescription.setText(response.description)
    }
    showImagePreview(response.image, response.title)
    setSize(response.size)
    setColor(response.color)
  }

  private fun setColor(colors: List<ColorResponse>) {
    colors.forEach {
      addColorChip(it.id, it.color)
    }
  }

  private fun setSize(sizes: List<SizeResponse>) {
    sizes.forEach { size ->
      size.detail?.let {
        addSizeChip(size.id, DesignDetailMapper.mapToSizeDetailUiModel(it))
      }
    }
  }

  private fun showImagePreview(imageUri: Uri, imageName: String) {
    showImagePreview(imageName)
    setImage(imageUri)
  }

  private fun showImagePreview(imageUrl: String, imageName: String) {
    showImagePreview(imageName)
    setImage(imageUrl)
  }

  private fun showImagePreview(imageName: String) {
    with(binding.layoutAddOrEditImage) {
      groupFilledImageState.show()
      groupEmptyImageState.remove()
      textViewPreviewDesignImageName.text = imageName
    }
    setClickable(false)
  }

  private fun setImage(imageUrl: String) {
    context?.let { context ->
      ImageLoader.loadImageUrl(context, imageUrl,
          binding.layoutAddOrEditImage.imageViewPreviewDesignImage)
    }
  }

  private fun setImage(imageUri: Uri) {
    binding.layoutAddOrEditImage.imageViewPreviewDesignImage.setImageURI(imageUri)
  }
}