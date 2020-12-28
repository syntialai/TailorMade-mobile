package com.future.tailormade_design_detail.feature.designDetail.view

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.future.tailormade.base.view.BaseFragment
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.util.extension.remove
import com.future.tailormade.util.extension.show
import com.future.tailormade.util.image.ImageLoader
import com.future.tailormade_auth.core.repository.impl.AuthSharedPrefRepository
import com.future.tailormade_design_detail.R
import com.future.tailormade_design_detail.core.model.ui.SizeDetailUiModel
import com.future.tailormade_design_detail.core.model.ui.SizeUiModel
import com.future.tailormade_design_detail.core.model.response.ColorResponse
import com.future.tailormade_design_detail.databinding.FragmentDesignDetailBinding
import com.future.tailormade_design_detail.feature.designDetail.viewModel.DesignDetailViewModel
import com.future.tailormade_router.actions.Action
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi

@AndroidEntryPoint
class DesignDetailFragment : BaseFragment() {

  companion object {
    private const val DESCRIPTION_MAX_LINES = 3

    fun newInstance() = DesignDetailFragment()
  }

  @Inject lateinit var authSharedPrefRepository: AuthSharedPrefRepository

  private val viewModel: DesignDetailViewModel by viewModels()

  private lateinit var binding: FragmentDesignDetailBinding

  override fun getLogName() =
      "com.future.tailormade_design_detail.feature.designDetail.view.DesignDetailFragment"

  override fun getViewModel(): BaseViewModel = viewModel

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View {
    binding = FragmentDesignDetailBinding.inflate(inflater, container, false)
    binding.layoutDesignDetailGeneralInfo.buttonEditDesignDetail.setOnClickListener {
      // TODO: go to edit design and pass design detail response
    }
    setupBottomNav()
    if (authSharedPrefRepository.userRole != 0) {
      hideCustomerFeatures()
    }
    return binding.root
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when(item.itemId) {
      R.id.item_search -> {
        goToSearch()
        true
      }
      else -> super.onOptionsItemSelected(item)
    }
  }

  private fun goToSearch() {
    context?.let { context ->
      startActivity(Action.goToSearch(context))
    }
  }

  @InternalCoroutinesApi
  @ExperimentalCoroutinesApi
  override fun setupFragmentObserver() {
    super.setupFragmentObserver()

    activity?.let { activity ->
      val id = (activity as DesignDetailActivity).designDetailId
      if (id.isNotBlank()) {
        viewModel.fetchDesignDetailData(id)
      }
    }

    viewModel.designDetailUiModel.observe(viewLifecycleOwner, {
      setupGeneralInfoLayout(it.title, it.tailorId, it.tailorName, it.image)
      setupGeneralInfoPrice(it.price, it.discount)
      setupChooseSizeChips(it.size)
      setupChooseColorChips(it.color)
      setupDescription(it.description)
    })
  }

  private fun getChooseSizeChip(index: Int, text: String): Chip {
    val chipBinding = layoutInflater.inflate(R.layout.item_choose_size_chip,
        binding.chipGroupChooseSize, false) as Chip
    with(chipBinding) {
      this.id = index
      this.text = text
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
    }
    return chipBinding
  }

  private fun hideCustomerFeatures() {
    with(binding) {
      bottomNavDesignDetail.remove()
      layoutDesignDetailGeneralInfo.buttonSwapFace.remove()
      layoutDesignDetailGeneralInfo.buttonEditDesignDetail.show()
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
    binding.bottomNavDesignDetail.setOnNavigationItemSelectedListener {
      when(it.itemId) {
        R.id.item_chat_tailor -> {
          // TODO: Go to chat
          true
        }
        R.id.item_add_to_cart -> {
          // TODO: Add item to cart
          true
        }
        R.id.item_button_order_now -> {
          // TODO: Go to checkout page
          true
        }
        else -> false
      }
    }
  }

  private fun setupChooseColorChips(colors: List<ColorResponse>) {
    with(binding.chipGroupChooseColor) {
      colors.forEachIndexed { index, color ->
        addView(getChooseColorChip(index, color.id, color.color))
      }
    }
  }

  private fun setupChooseSizeChips(sizes: List<SizeUiModel>) {
    with(binding.chipGroupChooseSize) {
      sizes.forEachIndexed { index, size ->
        addView(getChooseSizeChip(index, size.id))
      }
      this.setOnCheckedChangeListener { _, checkedId ->
        sizes[checkedId].detail?.let { setSizeDetailInfoData(it) }
      }
    }
  }

  private fun setupDescription(description: String) {
    with(binding) {
      textViewDesignDetailDescription.text = description
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
        // TODO: Go to face swap
      }
      buttonEditDesignDetail.setOnClickListener {
        // TODO: Go to edit design detail
      }
      textViewDesignDetailDesignedBy.setOnClickListener {
        // TODO: Go to tailor profile
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
        textViewDesignDetailBeforeDiscountPrice.text = price
        textViewDesignDetailBeforeDiscountPrice.text = it
      } ?: run {
        textViewDesignDetailPrice.text = price
      }
    }
  }

  private fun showDiscountPrice() {
    with(binding.layoutDesignDetailGeneralInfo) {
      textViewDesignDetailAfterDiscountPrice.show()
      textViewDesignDetailBeforeDiscountPrice.show()
    }
  }
}