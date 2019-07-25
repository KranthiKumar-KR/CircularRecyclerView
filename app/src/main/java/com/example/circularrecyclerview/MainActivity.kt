package com.example.circularrecyclerview

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.NumberPicker
import kotlinx.android.synthetic.main.activity_main.*
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {

    private var currentHourIndex = 0
    private var currentMinuteIndex = 0
    private var currentPeriodIndex = 0
    private var currentTime = "6:30 AM"
    private lateinit var hoursPicker: CircularRecyclerView
    private lateinit var minutesPicker: CircularRecyclerView
    private lateinit var periodPicker: NumberPicker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        hoursPicker = findViewById(R.id.hoursPicker)
        minutesPicker = findViewById(R.id.minutesPicker)
        periodPicker = findViewById(R.id.periodPicker)
        setupTimePicker()
        setCurrentTimePickerValue(currentTime)
    }

    private fun setupTimePicker() {
        // Setting the hours adapter and initial position
        val hoursAdapter = CircularRecyclerAdapter()
        hoursPicker.setPickerAdapter(hoursAdapter, hourItems, currentHourIndex)

        // Setting on Value Changed listener
        hoursPicker.setOnValueChangedListener {
            currentHourIndex = (it as Int) % hourItems.size
            currentTime =
                "${hourItems[currentHourIndex]}:${minuteItems[currentMinuteIndex]} ${periodItems[currentPeriodIndex]}"
            timeTextView.text = currentTime
        }

        // Setting the minutes adapter and initial position
        val minutesAdapter = CircularRecyclerAdapter()
        minutesPicker.setPickerAdapter(minutesAdapter, minuteItems, currentMinuteIndex)
        minutesPicker.setOnValueChangedListener {
            currentMinuteIndex = (it as Int) % minuteItems.size
            currentTime =
                "${hourItems[currentHourIndex]}:${minuteItems[currentMinuteIndex]} ${periodItems[currentPeriodIndex]}"
            timeTextView.text = currentTime
        }

        periodPicker.minValue = 0
        periodPicker.maxValue = periodItems.size - 1
        periodPicker.displayedValues = periodItems.toTypedArray()
        periodPicker.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        periodPicker.setOnValueChangedListener { _, _, newVal ->
            currentPeriodIndex = newVal
            currentTime =
                "${hourItems[currentHourIndex]}:${minuteItems[currentMinuteIndex]} ${periodItems[currentPeriodIndex]}"
            timeTextView.text = currentTime
        }
    }

    private fun setCurrentTimePickerValue(atTimeString: String) {
        if (atTimeString != "0:0 AM") {
            val hourValue = atTimeString.substringBefore(":")
            val hourIndex = if (hourItems.contains(hourValue)) hourItems.indexOf(hourValue) else 0
            hoursPicker.setCustomPickerValue(hourIndex, hourItems.size)
            currentHourIndex = hourIndex

            val minuteValue = atTimeString.substringBeforeLast(" ").substringAfter(":")
            val minuteIndex = if (minuteItems.contains(minuteValue)) minuteItems.indexOf(minuteValue) else 0
            minutesPicker.setCustomPickerValue(minuteIndex, minuteItems.size)
            currentMinuteIndex = minuteIndex

            val periodValue = atTimeString.substringAfterLast(" ")
            val periodIndex = if (periodItems.contains(periodValue)) periodItems.indexOf(periodValue) else 0
            periodPicker.value = periodIndex
            currentPeriodIndex = periodIndex
        }
    }

    companion object {
        private val hourItems = (1..12).map { it.toString() }
        private val decimalFormat = DecimalFormat("00")
        private var minuteItems: MutableList<String> =
            ((1..59).map { decimalFormat.format(it) }).toMutableList().apply {
                add("00")
            }
        private val periodItems = listOf("AM", "PM")
    }
}
