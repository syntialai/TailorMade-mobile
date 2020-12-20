package com.future.tailormade_dls.util.adapter

import android.graphics.Paint
import android.widget.TextView

import androidx.databinding.BindingAdapter

@BindingAdapter("strikeThrough")
fun strikeThrough(textView: TextView, strikeThrough: Boolean) {
  if (strikeThrough) {
    textView.paintFlags = textView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
  } else {
    textView.paintFlags = textView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
  }
}