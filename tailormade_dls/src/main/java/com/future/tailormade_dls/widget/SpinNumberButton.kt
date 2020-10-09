package com.future.tailormade_dls.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.future.tailormade_dls.databinding.LayoutSpinButtonNumberBinding

class SpinNumberButton constructor(context: Context, attrs: AttributeSet) :
    FrameLayout(context, attrs) {

    companion object {
        const val DEFAULT_VALUE = 1
    }

    private val layoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    private val viewBinding = LayoutSpinButtonNumberBinding.inflate(layoutInflater, this, true)

    private var spinValue: Int = 0

    init {
        setValue(DEFAULT_VALUE)
        disableReduceButton()
    }

    fun add() {
        spinValue++
        setValueText()
    }

    fun reduce() {
        if (spinValue > 1) {
            spinValue--
            setValueText()
        }
    }

    fun getValue(): Int = spinValue

    fun setValue(value: Int) {
        spinValue = value
        setValueText()
    }

    fun setValueText() {
        viewBinding.spinNumberValue.text = spinValue.toString()
    }

    fun enableReduceButton() {
        viewBinding.spinButtonNumberReduce.isEnabled = true
    }

    fun disableReduceButton() {
        viewBinding.spinButtonNumberReduce.isEnabled = false
    }
}