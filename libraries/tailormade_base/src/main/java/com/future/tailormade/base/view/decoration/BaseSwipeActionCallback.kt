package com.future.tailormade.base.view.decoration

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.future.tailormade.util.extension.orZero

abstract class BaseSwipeActionCallback(private val backgroundColor: Int,
    private var actionDrawable: Drawable?) : ItemTouchHelper.Callback() {

  private val _clearPaint = Paint()
  private val _background = ColorDrawable()
  private val _intrinsicWidth = actionDrawable?.intrinsicWidth.orZero()
  private val _intrinsicHeight = actionDrawable?.intrinsicHeight.orZero()

  override fun getMovementFlags(recyclerView: RecyclerView,
      viewHolder: RecyclerView.ViewHolder): Int {
    return makeMovementFlags(0, ItemTouchHelper.LEFT)
  }

  override fun onChildDraw(canvas: Canvas, recyclerView: RecyclerView,
      viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float,
      actionState: Int, isCurrentlyActive: Boolean) {
    super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState,
        isCurrentlyActive)

    val itemView = viewHolder.itemView
    val itemHeight = itemView.height

    if (isCancelled(dX, isCurrentlyActive)) {
      clearCanvas(canvas, itemView.right.plus(dX), itemView.top.toFloat(),
          itemView.right.toFloat(), itemView.bottom.toFloat())
      super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState,
          isCurrentlyActive)
      return
    }

    _background.apply {
      color = backgroundColor
      setBounds(itemView.right.plus(dX).toInt(), itemView.top, itemView.right,
          itemView.bottom)
      draw(canvas)
    }

    val deleteIconTop = itemView.top + (itemHeight - _intrinsicHeight) / 2
    val deleteIconMargin = (itemHeight - _intrinsicHeight) / 2
    val deleteIconLeft = itemView.right - deleteIconMargin - _intrinsicWidth
    val deleteIconRight = itemView.right - deleteIconMargin
    val deleteIconBottom = deleteIconTop + _intrinsicHeight

    actionDrawable?.apply {
      setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
      draw(canvas)
    }

    super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState,
        isCurrentlyActive)
  }

  override fun onMove(recyclerView: RecyclerView,
      viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
    return false
  }

  private fun clearCanvas(canvas: Canvas, left: Float, top: Float, right: Float,
      bottom: Float) {
    canvas.drawRect(left, top, right, bottom, _clearPaint)
  }

  private fun isCancelled(dX: Float, isCurrentlyActive: Boolean) = dX.compareTo(
      0) == 0 && isCurrentlyActive.not()
}