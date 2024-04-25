package com.example.timevision_application

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import java.util.*

class Register : AppCompatActivity() {
    private lateinit var dateOfBirthInput: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        // Initialize dateOfBirthInput
        dateOfBirthInput = findViewById(R.id.dateOfBirthInput)

        // Set up click listener for dateOfBirthInput
        dateOfBirthInput.setOnClickListener {
            showDatePicker()
        }

        // Apply edge-to-edge display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.register_main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    private fun showDatePicker() {
        val builder = MaterialDatePicker.Builder.datePicker()
        val picker = builder.build()

        picker.addOnPositiveButtonClickListener { selection ->
            // Convert selection to a Calendar instance
            val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            calendar.timeInMillis = selection

            // Format the selected date
            val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
            val month = calendar.get(Calendar.MONTH) + 1 // Month is zero-based
            val year = calendar.get(Calendar.YEAR)

            val selectedDate = "$dayOfMonth/$month/$year"

            // Set the formatted date to dateOfBirthInput
            dateOfBirthInput.setText(selectedDate)
        }
        picker.show(supportFragmentManager, picker.toString())
    }
}