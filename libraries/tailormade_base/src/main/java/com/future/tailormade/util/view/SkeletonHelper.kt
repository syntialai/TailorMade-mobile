package com.future.tailormade.util.view

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ethanhua.skeleton.RecyclerViewSkeletonScreen
import com.ethanhua.skeleton.Skeleton
import com.ethanhua.skeleton.SkeletonScreen
import com.ethanhua.skeleton.ViewSkeletonScreen
import com.future.tailormade.R
import com.future.tailormade.config.Constants

object SkeletonHelper {

  fun getSkeleton(view: View, layoutId: Int): ViewSkeletonScreen.Builder = Skeleton.bind(view)
      .load(layoutId)
      .shimmer(true)
      .angle(30)

  fun getRecyclerViewSkeleton(
      recyclerView: RecyclerView, layoutId: Int): RecyclerViewSkeletonScreen.Builder? =
      Skeleton.bind(recyclerView)
          .shimmer(true)
          .color(R.color.color_black_54)
          .angle(30)
          .count(10)
          .load(layoutId)
}