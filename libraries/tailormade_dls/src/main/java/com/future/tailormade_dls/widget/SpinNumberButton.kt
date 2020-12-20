package com.future.tailormade_dls.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.future.tailormade_dls.databinding.LayoutSpinButtonNumberBinding

class SpinNumberButton constructor(context: Context, attrs: AttributeSet) :
    LinearLayout(context, attrs) {

  companion object {
    const val DEFAULT_VALUE = 1
  }

  private val layoutInflater =
      context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
  private val binding = LayoutSpinButtonNumberBinding.inflate(layoutInflater, this, true)

  private var spinValue: Int = 0
  private var addSpinNumberButtonListener: ((Int) -> Unit)? = null
  private var reduceSpinNumberButtonListener: ((Int) -> Unit)? = null

  init {
    setValue(DEFAULT_VALUE)
    disableReduceButton()
  }

  fun add() {
    spinValue.inc()
    setValueText()
    addSpinNumberButtonListener?.invoke(spinValue)
  }

  fun reduce() {
    if (spinValue > 1) {
      spinValue.dec()
      setValueText()
      reduceSpinNumberButtonListener?.invoke(spinValue)
    }
  }

  fun getValue(): Int = spinValue

  fun setValue(value: Int) {
    spinValue = value
    setValueText()
  }

  fun setAddSpinNumberButtonListener(listener: (Int) -> Unit) {
    addSpinNumberButtonListener = listener
  }

  fun setReduceSpinNumberButtonListener(listener: (Int) -> Unit) {
    reduceSpinNumberButtonListener = listener
  }

  private fun setValueText() {
    binding.spinNumberValue.text = spinValue.toString()
    enableReduceButton()
  }

  private fun enableReduceButton() {
    if (spinValue > 1) {
      binding.spinButtonNumberReduce.isEnabled = true
    } else {
      disableReduceButton()
    }
  }

  private fun disableReduceButton() {
    binding.spinButtonNumberReduce.isEnabled = false
  }
}