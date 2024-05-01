package com.example.timevision_application

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var startDatePicker: DatePicker
    private lateinit var endDatePicker: DatePicker
    private lateinit var reasonEditText: EditText
    private lateinit var commentsEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        startDatePicker = findViewById(R.id.startDatePicker)
        endDatePicker = findViewById(R.id.endDatePicker)
        reasonEditText = findViewById(R.id.reasonEditText)
        commentsEditText = findViewById(R.id.commentsEditText)

        val submitButton = findViewById<Button>(R.id.submitButton)

        submitButton.setOnClickListener {
            // Retrieve values from views
            val startDate = "${startDatePicker.month + 1}-${startDatePicker.dayOfMonth}-${startDatePicker.year}"
            val endDate = "${endDatePicker.month + 1}-${endDatePicker.dayOfMonth}-${endDatePicker.year}"
            val reason = reasonEditText.text.toString()
            val comments = commentsEditText.text.toString()

            // Save information
            saveInformation(startDate, endDate, reason, comments)

            // Clear form fields
            clearForm()
        }
    }

    private fun saveInformation(startDate: String, endDate: String, reason: String, comments: String) {
        val sharedPreferences: SharedPreferences = getSharedPreferences("AbsenceRequest", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("startDate", startDate)
        editor.putString("endDate", endDate)
        editor.putString("reason", reason)
        editor.putString("comments", comments)
        editor.apply()
        showToast("Absence request saved.")
    }

    private fun clearForm() {
        startDatePicker.updateDate(2022, 0, 1)
        endDatePicker.updateDate(2022, 0, 1)
        reasonEditText.text.clear()
        commentsEditText.text.clear()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
