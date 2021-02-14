package com.future.tailormade_design_detail.feature.designDetail.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.future.tailormade.base.repository.AuthSharedPrefRepository
import com.future.tailormade.base.view.BaseActivity
import com.future.tailormade.base.view.BaseFragment
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.config.Constants
import com.future.tailormade.util.extension.hide
import com.future.tailormade.util.extension.remove
import com.future.tailormade.util.extension.show
import com.future.tailormade.util.extension.strikeThrough
import com.future.tailormade.util.image.ImageLoader
import com.future.tailormade_design_detail.R
import com.future.tailormade_design_detail.core.model.response.ColorResponse
import com.future.tailormade_design_detail.core.model.ui.SizeDetailUiModel
import com.future.tailormade_design_detail.core.model.ui.SizeUiModel
import com.future.tailormade_design_detail.databinding.FragmentDesignDetailBinding
import com.future.tailormade_design_detail.feature.designDetail.viewModel.DesignDetailViewModel
import com.future.tailormade_router.actions.Action
import com.future.tailormade_router.actions.UserAction
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi

@AndroidEntryPoint
class DesignDetailFragment : BaseFragment() {

  companion object {
    private const val DESCRIPTION_MAX_LINES = 3
    private const val TYPE_ADD_TO_CART = "TYPE_ADD_TO_CART"
    private const val TYPE_CHECKOUT = "TYPE_CHECKOUT"
    private const val IMAGE_REQUEST_CODE = 20

    fun newInstance() = DesignDetailFragment()
  }

  @Inject lateinit var authSharedPrefRepository: AuthSharedPrefRepository

  private val viewModel: DesignDetailViewModel by viewModels()

  private lateinit var binding: FragmentDesignDetailBinding

  override fun getLogName() =
      "com.future.tailormade_design_detail.feature.designDetail.view.DesignDetailFragment"

  override fun getScreenName(): String = "Design Detail"

  override fun getViewModel(): BaseViewModel = viewModel

  override fun onNavigationIconClicked() {
    activity?.finish()
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View {
    binding = FragmentDesignDetailBinding.inflate(inflater, container, false)
    setupBottomNav()
    if (authSharedPrefRepository.isTailor()) {
      hideCustomerFeatures()
    }
    showSkeleton(binding.layoutDesignDetail, R.layout.layout_design_detail_skeleton)
    return binding.root
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_REQUEST_CODE) {
      data?.data?.let { imageUri ->
        activity?.contentResolver?.let {
          goToSwapFace(imageUri)
        }
      }
    }
  }

  @ExperimentalCoroutinesApi
  @InternalCoroutinesApi
  override fun setupFragmentObserver() {
    super.setupFragmentObserver()

    activity?.let { activity ->
      val id = (activity as DesignDetailActivity).designDetailId
      if (id.isNotBlank()) {
        viewModel.fetchDesignDetailData(id)
      }
    }

    viewModel.designDetailUiModel.observe(viewLifecycleOwner, {
      it?.let { designDetailUiModel ->
        setupGeneralInfoLayout(designDetailUiModel.title, designDetailUiModel.tailorId,
            designDetailUiModel.tailorName, designDetailUiModel.image)
        setupGeneralInfoPrice(designDetailUiModel.price, designDetailUiModel.discount)
        setupChooseSizeChips(designDetailUiModel.size)
        setupChooseColorChips(designDetailUiModel.color)
        setupDescription(designDetailUiModel.description)
        hideSkeleton()
      }
    })

    if (authSharedPrefRepository.isUser()) {
      viewModel.designDetailResponse.observe(viewLifecycleOwner, {
        it?.let { designDetailResponse ->
          viewModel.setAddToCartRequest(designDetailResponse)
        }
      })
      viewModel.isAddedToCart.observe(viewLifecycleOwner, {
        it?.let { isAdded ->
          if (isAdded.second.isNullOrBlank().not()) {
            when (isAdded.first) {
              TYPE_ADD_TO_CART -> showSuccessToast(R.string.design_added_to_cart)
              TYPE_CHECKOUT -> checkoutItem(isAdded.second.orEmpty())
            }
          }
        }
      })
    }
  }

  private fun checkoutItem(id: String) {
    context?.let {
      UserAction.goToCheckout(it, id)
    }
  }

  private fun getChooseSizeChip(index: Int, text: String): Chip {
    val chipBinding = layoutInflater.inflate(R.layout.item_choose_size_chip,
        binding.chipGroupChooseSize, false) as Chip
    with(chipBinding) {
      this.id = index
      this.text = text
      this.isChecked = index == 0
    }
    return chipBinding
  }

  private fun getChooseColorChip(index: Int, text: String, color: String): Chip {
    val chipBinding = layoutInflater.inflate(R.layout.item_choose_color_chip,
        binding.chipGroupChooseColor, false) as Chip
    with(chipBinding) {
      this.id = index
      this.text = text
      this.chipIconTint = ColorStateList.valueOf(Color.parseColor(color))
      this.isChecked = index == 0
    }
    return chipBinding
  }

  private fun goToChat() {
    context?.let { context ->
      viewModel.designDetailResponse.value?.let {
        Action.goToChatRoom(context, it.tailorId, it.tailorName)
      }
    }
  }

  private fun goToSwapFace(bitmapSource: Uri) {
    context?.let { context ->
      UserAction.goToSwapFace(context, bitmapSource,
          viewModel.designDetailUiModel.value?.image.orEmpty())
    }
  }

  private fun goToTailorProfile(tailorId: String, tailorName: String) {
    context?.let { context ->
      UserAction.goToTailorProfile(context, tailorId, tailorName)
    }
  }

  private fun hideCustomerFeatures() {
    with(binding) {
      layoutDesignDetailBottomNav.root.remove()
      layoutDesignDetailGeneralInfo.buttonSwapFace.remove()
      layoutDesignDetailGeneralInfo.buttonEditDesignDetail.show()
    }
  }

  private fun openGallery() {
    (activity as BaseActivity).checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE,
        BaseActivity.READ_EXTERNAL_STORAGE_PERMISSION) {
      val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
        type = Constants.TYPE_IMAGE_ALL
      }
      startActivityForResult(galleryIntent, IMAGE_REQUEST_CODE)
    }
  }

  private fun setSizeDetailInfoData(sizeDetailUiModel: SizeDetailUiModel) {
    with(binding.layoutSizeInformationDetail) {
      textViewSizeChest.text = sizeDetailUiModel.chest
      textViewSizeHips.text = sizeDetailUiModel.hips
      textViewSizeWaist.text = sizeDetailUiModel.waist
      textViewSizeInseam.text = sizeDetailUiModel.inseam
      textViewSizeNeckToWaist.text = sizeDetailUiModel.neckToWaist
    }
  }

  private fun setupBottomNav() {
    with(binding.layoutDesignDetailBottomNav) {
      buttonChatTailorDesignDetail.setOnClickListener {
        goToChat()
      }
      buttonAddToCartDesignDetail.setOnClickListener {
        viewModel.addToCart(TYPE_ADD_TO_CART)
      }
      buttonOrderNowDesignDetail.setOnClickListener {
        viewModel.addToCart(TYPE_CHECKOUT)
      }
    }
  }

  private fun setupChooseColorChips(colors: List<ColorResponse>) {
    with(binding.chipGroupChooseColor) {
      removeAllViews()
      colors.forEachIndexed { index, color ->
        addView(getChooseColorChip(index, color.name, color.color))
      }
      setOnCheckedChangeListener { _, checkedId ->
        viewModel.setColorRequest(colors[checkedId].name)
      }
    }
  }

  private fun setupChooseSizeChips(sizes: List<SizeUiModel>) {
    with(binding.chipGroupChooseSize) {
      removeAllViews()
      sizes.forEachIndexed { index, size ->
        addView(getChooseSizeChip(index, size.name.orEmpty()))
      }
      setOnCheckedChangeListener { _, checkedId ->
        sizes[checkedId].detail?.let {
          setSizeDetailInfoData(it)
          viewModel.setSizeRequest(checkedId)
        }
      }
    }
  }

  private fun setupDescription(description: String) {
    with(binding) {
      textViewDesignDetailDescription.text = description

      if (textViewDesignDetailDescription.lineCount > DESCRIPTION_MAX_LINES) {
        textViewReadMore.show()
      }

      textViewReadMore.setOnClickListener {
        with(textViewDesignDetailDescription) {
          if (maxLines == DESCRIPTION_MAX_LINES) {
            this.maxLines = Integer.MAX_VALUE
            this.ellipsize = null
          } else {
            this.maxLines = DESCRIPTION_MAX_LINES
            this.ellipsize = TextUtils.TruncateAt.END
          }
        }
      }
    }
  }

  private fun setupGeneralInfoLayout(title: String, tailorId: String, tailorName: String,
      imageUrl: String) {
    with(binding.layoutDesignDetailGeneralInfo) {
      buttonSwapFace.setOnClickListener {
        openGallery()
      }
      buttonEditDesignDetail.setOnClickListener {
        viewModel.designDetailResponse.value?.let { designDetailResponse ->
          findNavController().navigate(
              DesignDetailFragmentDirections.actionDesignDetailFragmentToAddOrEditDesignFragment(
                  designDetailResponse))
        }
      }
      textViewDesignDetailDesignedBy.setOnClickListener {
        if (authSharedPrefRepository.isUser()) {
          goToTailorProfile(tailorId, tailorName)
        }
      }

      textViewDesignDetailTitle.text = title
      textViewDesignDetailDesignedBy.text = tailorName

      context?.let { context ->
        ImageLoader.loadImageUrl(context, imageUrl, imageViewDesignDetail)
      }
    }
  }

  private fun setupGeneralInfoPrice(price: String, discount: String?) {
    with(binding.layoutDesignDetailGeneralInfo) {
      discount?.let {
        showDiscountPrice()
        textViewDesignDetailAfterDiscountPrice.text = it
        textViewDesignDetailBeforeDiscountPrice.text = price
        textViewDesignDetailBeforeDiscountPrice.strikeThrough()
      } ?: run {
        textViewDesignDetailPrice.text = price
      }
    }
  }

  private fun showDiscountPrice() {
    with(binding.layoutDesignDetailGeneralInfo) {
      textViewDesignDetailAfterDiscountPrice.show()
      textViewDesignDetailBeforeDiscountPrice.show()
      textViewDesignDetailPrice.hide()
    }
  }
}