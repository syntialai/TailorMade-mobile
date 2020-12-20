package com.future.tailormade_profile.feature.profile.view

import android.R.attr.spacing
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.setPadding
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.future.tailormade.base.view.BaseFragment
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.util.extension.orZero
import com.future.tailormade_profile.R
import com.future.tailormade_profile.databinding.FragmentProfileDesignBinding
import com.future.tailormade_profile.feature.profile.adapter.ProfileDesignAdapter
import com.future.tailormade_profile.feature.profile.viewModel.ProfileDesignViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
class ProfileDesignFragment : BaseFragment() {

  companion object {
    fun newInstance() = ProfileDesignFragment()
  }

  private lateinit var binding: FragmentProfileDesignBinding

  private val viewModel: ProfileDesignViewModel by viewModels()
  private val profileDesignAdapter by lazy {
    ProfileDesignAdapter(::goToDesignDetailPage)
  }

  override fun getLogName() = "com.future.tailormade_profile.feature.profile.view.ProfileDesignFragment"

  override fun getViewModel(): BaseViewModel = viewModel

  @ExperimentalCoroutinesApi
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View {
    binding = FragmentProfileDesignBinding.inflate(inflater, container, false)
    setupRecyclerView()
    return binding.root
  }

  @ExperimentalCoroutinesApi
  override fun setupFragmentObserver() {
    super.setupFragmentObserver()

    viewModel.fetchImages()
    viewModel.images.observe(viewLifecycleOwner, {
      profileDesignAdapter.submitList(it)
    })
  }

  private fun goToDesignDetailPage(id: String) {
    // TODO: Go to design detail page
  }

  @ExperimentalCoroutinesApi
  private fun setupRecyclerView() {
    with(binding.recyclerViewProfileDesign) {
      layoutManager = GridLayoutManager(context, 3)
      adapter = profileDesignAdapter
      setPadding(resources.getDimensionPixelSize(R.dimen.dp_4))
      clipToPadding = false
      clipChildren = false

      addItemDecoration(object : ItemDecoration() {

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView,
            state: RecyclerView.State) {
          outRect.set(spacing, spacing, spacing, spacing)
        }
      })

      addOnScrollListener(object : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
          super.onScrolled(recyclerView, dx, dy)

          if (isLastItemViewed(recyclerView, viewModel.images.value?.size.orZero())) {
            viewModel.fetchMore()
          }
        }
      })
    }
  }
}