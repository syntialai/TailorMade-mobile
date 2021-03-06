package com.future.tailormade_profile.feature.profile.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.future.tailormade.base.view.BaseFragment
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.util.extension.orZero
import com.future.tailormade.util.extension.remove
import com.future.tailormade.util.extension.show
import com.future.tailormade_profile.R
import com.future.tailormade_profile.databinding.FragmentProfileDesignBinding
import com.future.tailormade_profile.feature.profile.adapter.ProfileDesignAdapter
import com.future.tailormade_profile.feature.profile.viewModel.ProfileDesignViewModel
import com.future.tailormade_profile.feature.profile.viewModel.TailorProfileViewModel
import com.future.tailormade_router.actions.Action
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
class ProfileDesignFragment : BaseFragment() {

  companion object {
    fun newInstance() = ProfileDesignFragment()
  }

  private lateinit var binding: FragmentProfileDesignBinding

  private val profileViewModel: TailorProfileViewModel by activityViewModels()
  private val viewModel: ProfileDesignViewModel by viewModels()

  private val profileDesignAdapter by lazy {
    ProfileDesignAdapter(::goToDesignDetailPage)
  }

  override fun getLogName() =
      "com.future.tailormade_profile.feature.profile.view.ProfileDesignFragment"

  override fun getViewModel(): BaseViewModel = viewModel

  @ExperimentalCoroutinesApi
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View {
    binding = FragmentProfileDesignBinding.inflate(inflater, container, false)
    setupRecyclerView()
    setupSkeleton()
    return binding.root
  }

  @ExperimentalCoroutinesApi
  override fun setupFragmentObserver() {
    super.setupFragmentObserver()

    profileViewModel.tailorId.observe(viewLifecycleOwner, {
      viewModel.setId(it)
      viewModel.fetchImages()
    })
    viewModel.images.observe(viewLifecycleOwner, {
      profileDesignAdapter.submitList(it)
      if (it.isEmpty()) {
        showEmptyState()
      } else {
        showRecyclerView()
      }
      binding.recyclerViewProfileDesign.post {
        hideSkeleton()
      }
    })
  }

  private fun getDividerItemDecoration(context: Context, orientation: Int) = DividerItemDecoration(
      context, orientation).apply {
    ContextCompat.getDrawable(context, R.drawable.profile_design_item_separator)?.let {
      setDrawable(it)
    }
  }

  private fun goToDesignDetailPage(id: String) {
    context?.let {
      Action.goToDesignDetail(it, id)
    }
  }

  @ExperimentalCoroutinesApi
  private fun setupRecyclerView() {
    with(binding.recyclerViewProfileDesign) {
      layoutManager = GridLayoutManager(context, 2)
      adapter = profileDesignAdapter

      addItemDecoration(getDividerItemDecoration(context, GridLayoutManager.VERTICAL))
      addItemDecoration(getDividerItemDecoration(context, GridLayoutManager.HORIZONTAL))

      addOnScrollListener(object : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
          super.onScrolled(recyclerView, dx, dy)

          if (isLastItemViewed(recyclerView, viewModel.images.value?.size.orZero(), false)) {
            viewModel.fetchMore()
          }
        }
      })
    }
  }

  private fun setupSkeleton() {
    skeletonScreen = getSkeleton(binding.recyclerViewProfileDesign,
        R.layout.layout_profile_design_image_skeleton)?.count(
        21)?.adapter(profileDesignAdapter)?.show()
  }

  private fun showEmptyState() {
    with(binding) {
      recyclerViewProfileDesign.remove()
      imageViewDesignEmptyState.show()
    }
  }

  private fun showRecyclerView() {
    with(binding) {
      recyclerViewProfileDesign.show()
      imageViewDesignEmptyState.remove()
    }
  }
}