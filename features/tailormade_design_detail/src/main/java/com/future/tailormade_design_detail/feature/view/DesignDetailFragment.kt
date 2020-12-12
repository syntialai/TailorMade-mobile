package com.future.tailormade_design_detail.feature.view

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
import com.future.tailormade_design_detail.R
import com.future.tailormade_design_detail.core.model.response.ColorResponse
import com.future.tailormade_design_detail.core.model.response.SizeDetailResponse
import com.future.tailormade_design_detail.core.model.response.SizeResponse
import com.future.tailormade_design_detail.databinding.FragmentDesignDetailBinding
import com.future.tailormade_design_detail.databinding.ItemChooseColorChipBinding
import com.future.tailormade_design_detail.databinding.ItemChooseSizeChipBinding
import com.future.tailormade_design_detail.feature.viewModel.DesignDetailViewModel
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DesignDetailFragment : BaseFragment() {

  companion object {
    private const val DESCRIPTION_MAX_LINES = 3
    private const val NO_DISCOUNT = 0.0

    fun newInstance() = DesignDetailFragment()
  }

  private val viewModel: DesignDetailViewModel by viewModels()

  private lateinit var binding: FragmentDesignDetailBinding

  override fun getLogName(): String = "com.future.tailormade_design_detail.feature.view.DesignDetailFragment"

  override fun getViewModel(): BaseViewModel = viewModel

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View {
    binding = FragmentDesignDetailBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when(item.itemId) {
      R.id.item_search -> {
        // TODO: Go to search
        true
      }
      else -> super.onOptionsItemSelected(item)
    }
  }

  private fun getChooseSizeChip(index: Int, text: String): Chip {
    val chipBinding =
      ItemChooseSizeChipBinding.inflate(layoutInflater, binding.chipGroupChooseSize, true)
    with(chipBinding.chipChooseSize) {
      this.id = index
      this.text = text
    }
    return chipBinding.root
  }

  private fun getChooseColorChip(index: Int, text: String, color: String): Chip {
    val chipBinding =
      ItemChooseColorChipBinding.inflate(layoutInflater, binding.chipGroupChooseColor, true)
    with(chipBinding.chipChooseColor) {
      this.id = index
      this.text = text
      this.chipIconTint = ColorStateList.valueOf(Color.parseColor(color))
    }
    return chipBinding.root
  }

  private fun hideCustomerFeatures() {
    with(binding) {
      bottomNavDesignDetail.remove()
      layoutDesignDetailGeneralInfo.buttonSwapFace.remove()
    }
  }

  private fun setSizeDetailInfoData(sizeDetailResponse: SizeDetailResponse?) {
    with(binding.layoutSizeInformationDetail) {
      textViewSizeChest.text = sizeDetailResponse?.chest.toString()
      textViewSizeHips.text = sizeDetailResponse?.hips.toString()
      textViewSizeWaist.text = sizeDetailResponse?.waist.toString()
      textViewSizeInseam.text = sizeDetailResponse?.inseam.toString()
      textViewSizeNeckToWaist.text = sizeDetailResponse?.neckToWaist.toString()
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

  private fun setupChooseSizeChips(sizes: List<SizeResponse>) {
    with(binding.chipGroupChooseSize) {
      sizes.forEachIndexed { index, size ->
        addView(getChooseSizeChip(index, size.id))
      }
      this.setOnCheckedChangeListener { group, checkedId ->
        setSizeDetailInfoData(sizes[checkedId].detail)
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

  private fun setupGeneralInfoLayout(name: String, tailorName: String, imageUrl: String) {
    with(binding.layoutDesignDetailGeneralInfo) {
      buttonSwapFace.setOnClickListener {
        // TODO: Go to face swap
      }
      buttonEditDesignDetail.setOnClickListener {
        // TODO: Go to edit design detail
      }
      textViewDesignDetailName.text = name
      textViewDesignDetailDesignedBy.text = tailorName

      context?.let { context ->
        ImageLoader.loadImageUrl(context, imageUrl, imageViewDesignDetail)
      }
    }
  }

  private fun setupGeneralInfoPrice(price: Double, discount: Double) {
    with(binding.layoutDesignDetailGeneralInfo) {
      // TODO: Add money converter to extension and use it here
      if (discount == NO_DISCOUNT) {
        textViewDesignDetailPrice.text = price.toString()
      } else {
        showDiscountPrice()
        textViewDesignDetailBeforeDiscountPrice.text = price.toString()
        textViewDesignDetailBeforeDiscountPrice.text = (price - discount).toString()
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