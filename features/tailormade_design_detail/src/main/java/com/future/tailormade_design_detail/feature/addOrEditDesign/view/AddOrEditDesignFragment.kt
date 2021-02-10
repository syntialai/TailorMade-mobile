package com.future.tailormade_design_detail.feature.addOrEditDesign.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.future.tailormade.base.view.BaseActivity
import com.future.tailormade.base.view.BaseFragment
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.config.Constants
import com.future.tailormade.util.extension.remove
import com.future.tailormade.util.extension.show
import com.future.tailormade.util.extension.text
import com.future.tailormade.util.extension.validateInput
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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@AndroidEntryPoint
class AddOrEditDesignFragment : BaseFragment() {

  companion object {
    private const val GALLERY_REQUEST_CODE = 10

    fun newInstance() = AddOrEditDesignFragment()
  }

  private lateinit var binding: FragmentAddOrEditDesignBinding

  override fun onNavigationIconClicked() {
    activity?.finish()
  }

  private val args: AddOrEditDesignFragmentArgs by navArgs()
  private val viewModel: AddOrEditDesignViewModel by viewModels()

  override fun getLogName() =
      "com.future.tailormade_design_detail.feature.addOrEditDesign.view.AddOrEditDesignFragment"

  override fun getScreenName() = getString(args.designDetail?.let {
    R.string.edit_design_label
  } ?: R.string.add_design_label)

  override fun getViewModel(): BaseViewModel = viewModel

  @FlowPreview
  @ExperimentalCoroutinesApi
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
      buttonSaveDesignInfo.text = getScreenName()
      buttonSaveDesignInfo.setOnClickListener {
        validate()
      }
    }
    hideImagePreview()
    setupValidator()
    return binding.root
  }

  @RequiresApi(Build.VERSION_CODES.O)
  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (resultCode == Activity.RESULT_OK && requestCode == GALLERY_REQUEST_CODE) {
      data?.data?.let { imageUri ->
        activity?.contentResolver?.let {
          ImageHelper.getFileAbsolutePath(it, imageUri)?.let { path ->
            viewModel.setImageFile(path)
            if (isImagePreviewShown().not()) {
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
      it?.let { designDetail ->
        setData(designDetail)
      }
    })
    viewModel.isUpdated.observe(viewLifecycleOwner, {
      it?.let { isUpdated ->
        if (isUpdated) {
          onNavigationIconClicked()
          showSuccessToast()
        }
      }
    })
  }

  private fun showSuccessToast() {
    val messageId = args.designDetail?.let {
      R.string.design_added
    } ?: R.string.design_updated
    showSuccessToast(messageId)
  }

  private fun addSizeChip(text: String, sizeDetail: SizeDetailUiModel) {
    val chipBinding = layoutInflater.inflate(R.layout.item_choose_size_chip,
        binding.chipGroupDesignSize, false) as Chip
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

  @SuppressLint("ClickableViewAccessibility")
  private fun hideImagePreview() {
    with(binding.layoutAddOrEditImage) {
      groupFilledImageState.remove()
      groupEmptyImageState.show()
      root.setOnTouchListener { view, event ->
        if (event.action == MotionEvent.ACTION_DOWN) {
          view.performClick()
          openGallery()
        }
        false
      }
    }
    setClickable(true)
  }

  private fun isDiscountValid(text: String) = Pair(
      viewModel.isPriceValid(binding.editTextDesignPrice.text(), text),
      getString(R.string.design_discount_invalid))

  private fun isImagePreviewShown() = binding.layoutAddOrEditImage.groupFilledImageState.isShown

  private fun openGallery() {
    (activity as BaseActivity).checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE,
        BaseActivity.READ_EXTERNAL_STORAGE_PERMISSION) {
      val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
        type = Constants.TYPE_IMAGE_ALL
      }
      startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE)
    }
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
      addColorChip(it.name, it.color)
    }
  }

  private fun setSize(sizes: List<SizeResponse>) {
    sizes.forEach { size ->
      size.detail?.let {
        addSizeChip(size.name, DesignDetailMapper.mapToSizeDetailUiModel(it))
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

  @FlowPreview
  @ExperimentalCoroutinesApi
  private fun validate() {
    with(binding) {
      val name = editTextDesignName.text()
      val price = editTextDesignPrice.text()
      val discount = editTextDesignDiscount.text()
      val description = editTextDesignDescription.text()

      if (isDataValid(name, price, discount, description) && viewModel.validate()) {
        addOrUpdateDesign(name, price, discount, description)
      } else {
        setErrorMessage(name, price, discount, description)
      }
    }
  }

  private fun isDataValid(name: String, price: String, discount: String, description: String) =
      name.isNotBlank() && price.isNotBlank() && discount.isNotBlank() && description.isNotBlank()
      && viewModel.isPriceValid(price, discount)

  @ExperimentalCoroutinesApi
  @FlowPreview
  private fun addOrUpdateDesign(name: String, price: String, discount: String, description: String) {
    args.designDetail?.let {
      viewModel.updateDesign(name, price, discount, description)
    } ?: run {
      viewModel.addDesign(name, price, discount, description)
    }
  }

  private fun setErrorMessage(name: String, price: String, discount: String, description: String) {
    with(binding) {
      editTextDesignName.error = when {
        name.isBlank() -> getString(R.string.design_name_is_empty)
        else -> null
      }

      editTextDesignPrice.error = when {
        price.isBlank() -> getString(R.string.design_price_is_empty)
        else -> null
      }

      editTextDesignDiscount.error = when {
        discount.isBlank() -> getString(R.string.design_discount_is_empty)
        viewModel.isPriceValid(price, discount).not() -> getString(R.string.design_discount_invalid)
        else -> null
      }

      editTextDesignDescription.error = when {
        description.isBlank() -> getString(R.string.design_description_is_empty)
        else -> null
      }
    }
  }

  private fun setupValidator() {
    with(binding) {
      editTextDesignName.validateInput(textInputDesignName,
          getString(R.string.design_name_is_empty))
      editTextDesignPrice.validateInput(textInputDesignPrice,
          getString(R.string.design_price_is_empty))
      editTextDesignDiscount.validateInput(textInputDesignDiscount,
          getString(R.string.design_discount_is_empty), ::isDiscountValid)
      editTextDesignDescription.validateInput(textInputDesignDescription,
          getString(R.string.design_description_is_empty))
    }
  }
}