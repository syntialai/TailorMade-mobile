package com.future.tailormade_dls.widget

import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import java.time.LocalDateTime
import java.util.GregorianCalendar
import kotlinx.android.parcel.Parcelize

class DatePickerDialog {

  companion object {
    private const val MINIMUM_YEAR = 1900
    private const val MINIMUM_MONTH = 1
    private const val MINIMUM_DAY = 1
    private const val MINIMUM_TIME = 0
  }

  private val materialDatePicker = MaterialDatePicker.Builder.datePicker()

  init {
    setConstraints()
  }

  fun getDatePicker() = materialDatePicker

  private fun getCalendarTime(date: LocalDateTime): Long = GregorianCalendar.getInstance().apply {
    set(date.year, date.monthValue - 1, date.dayOfMonth)
  }.timeInMillis

  private fun getTodayDate() = LocalDateTime.now()

  private fun getMinimumDate() = LocalDateTime.of(MINIMUM_YEAR, MINIMUM_MONTH, MINIMUM_DAY,
      MINIMUM_TIME, MINIMUM_TIME)

  private fun limitDateRange(minDate: LocalDateTime = getMinimumDate(),
      maxDate: LocalDateTime = getTodayDate()): CalendarConstraints.Builder {
    val timeStart = getCalendarTime(minDate)
    val timeEnd = getCalendarTime(maxDate)

    return CalendarConstraints.Builder().apply {
      setStart(timeStart)
      setEnd(timeEnd)
      setValidator(RangeValidator(timeStart, timeEnd))
    }
  }

  private fun setConstraints() {
    materialDatePicker.setCalendarConstraints(limitDateRange().build())
  }

  @Parcelize
  internal class RangeValidator(private var minDate: Long,
      private var maxDate: Long) : CalendarConstraints.DateValidator {

    override fun isValid(date: Long): Boolean = date in minDate .. maxDate
  }
}