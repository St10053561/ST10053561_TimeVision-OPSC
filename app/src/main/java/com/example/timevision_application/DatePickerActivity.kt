package com.example.timevision_application

import android.app.Activity
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.datepicker.MaterialDatePicker
import java.util.Date
import java.util.Locale

class DatePickerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create a date range picker
        val materialDatePicker = MaterialDatePicker.Builder.dateRangePicker()
            // Set the title of the date picker
            .setTitleText("Select Date")
            // Set the initial selection to be from the start of this month to today
            .setSelection(
                androidx.core.util.Pair(
                    MaterialDatePicker.thisMonthInUtcMilliseconds(),
                    MaterialDatePicker.todayInUtcMilliseconds()
                )
            )
            // Set the text of the confirmation button
            .setPositiveButtonText("Submit")

            .build()

// Set what happens when the confirmation button is clicked
        materialDatePicker.addOnPositiveButtonClickListener { pair ->
            // Create a new intent
            val intent = Intent()
            // Put the start and end dates in the intent
            intent.putExtra("startDate", pair.first)
            intent.putExtra("endDate", pair.second)

            setResult(Activity.RESULT_OK, intent) // Set the result of this activity to be the intent

            finish()
        }

// Show the date picker
        materialDatePicker.show(supportFragmentManager, "DATE_PICKER")


    }
}