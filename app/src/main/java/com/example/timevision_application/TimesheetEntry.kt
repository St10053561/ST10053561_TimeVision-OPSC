package com.example.timevision_application

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.timevision_application.databinding.ActivityMainBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class TimesheetEntry : AppCompatActivity() {

    // Companion object for constants
    companion object {
        private const val IMAGE_PICKER_REQUEST_CODE = 100
    }

    // Global Variables
    private lateinit var browseFilesButton: ImageButton
    private lateinit var projectNameInput: TextInputEditText
    private lateinit var dateInput: TextInputEditText
    private lateinit var categoryInput: TextInputEditText
    private lateinit var startDateInput: TextInputEditText
    private lateinit var endDateInput: TextInputEditText
    private lateinit var startTimeInput: EditText
    private lateinit var endTimeInput: EditText
    private lateinit var totalDuration: TextInputEditText
    private lateinit var workDescriptionInput: TextInputEditText
    private lateinit var submitButton: Button

    // Variables for database
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    // Storage Reference
    private lateinit var storageReference: StorageReference
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timesheet_entry)

        // Initialize FirebaseAuth instance
        auth = FirebaseAuth.getInstance()

        // Initialize TextInputEditText fields
        projectNameInput = findViewById(R.id.projectNameInput)
        dateInput = findViewById(R.id.dateInput)
        categoryInput = findViewById(R.id.categoryInput)
        startDateInput = findViewById(R.id.startDateInput)
        endDateInput = findViewById(R.id.endDateInput)
        startTimeInput = findViewById(R.id.startTimeInput)
        endTimeInput = findViewById(R.id.endTimeInput)
        workDescriptionInput = findViewById(R.id.workDescriptionInput)
        submitButton = findViewById(R.id.submitButton)
        browseFilesButton = findViewById(R.id.browseFilesButton) // Initialize browseFilesButton

        // Set OnClickListener to open DatePickerDialog for dateInput
        dateInput.setOnClickListener {
            showDatePicker(dateInput)
        }

        // Set OnClickListener to open DatePickerDialog for startDateInput
        startDateInput.setOnClickListener {
            showDatePicker(startDateInput)
        }

        // Set OnClickListener to open DatePickerDialog for endDateInput
        endDateInput.setOnClickListener {
            showDatePicker(endDateInput)
        }

        // Set OnClickListener to open TimePickerDialog for startTimeInput
        startTimeInput.setOnClickListener {
            showTimePicker(startTimeInput)
        }

        // Set OnClickListener to open TimePickerDialog for endTimeInput
        endTimeInput.setOnClickListener {
            showTimePicker(endTimeInput)
        }

        browseFilesButton.setOnClickListener {
            // Add your browse files functionality here
            openImagePicker()
        }

        // Set OnClickListener for the submit button
        submitButton.setOnClickListener {
            /// Get the text entered by the user
            val projectName = projectNameInput.text.toString()
            val category = categoryInput.text.toString()
            val workDescription = workDescriptionInput.text.toString()
            val startDate = startDateInput.text.toString()
            val endDate = endDateInput.text.toString()
            val startTime = startTimeInput.text.toString()
            val endTime = endTimeInput.text.toString()

            // Calculate totalDuration
            val totalDurationValue = calculateTotalDuration(startTime, endTime)

            // Initialize the totalDuration property
            totalDuration = findViewById(R.id.totalDuration)

            // Update the totalDuration EditText
            totalDuration.setText(totalDurationValue)

            saveToDatabase(projectName, category, workDescription, startDate, endDate, startTime, endTime, totalDurationValue, imageUri)
        }
    }


    private fun showDatePicker(textInputEditText: TextInputEditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = "${selectedMonth + 1}/$selectedDay/$selectedYear"
                textInputEditText.setText(selectedDate)
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }

    private fun showTimePicker(editText: EditText) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            this,
            TimePickerDialog.OnTimeSetListener { _, selectedHour, selectedMinute ->
                val selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                editText.setText(selectedTime)
            },
            hour,
            minute,
            true
        )

        timePickerDialog.show()
    }

    private fun calculateTotalDuration(startTime: String, endTime: String): String {
        // Split the time strings into hours and minutes
        val startParts = startTime.split(":")
        val endParts = endTime.split(":")

        // Convert hours and minutes to minutes
        val startMinutes = startParts[0].toInt() * 60 + startParts[1].toInt()
        val endMinutes = endParts[0].toInt() * 60 + endParts[1].toInt()

        // Calculate the difference in minutes
        val durationMinutes = endMinutes - startMinutes

        // Convert the difference back to hours and minutes format
        val hours = durationMinutes / 60
        val minutes = durationMinutes % 60

        // Return the formatted total duration string
        return String.format("%02d:%02d", hours, minutes)
    }

    private fun saveToDatabase(
        projectName: String,
        category: String,
        workDescription: String,
        startDate: String,
        endDate: String,
        startTime: String,
        endTime: String,
        totalDuration: String,
        imageUri: Uri? // Pass the image URI as a parameter
    ) {
        // Code to save entry to a database
        val uid = auth.currentUser?.uid
        databaseReference = FirebaseDatabase.getInstance().getReference("TimeSheetEntries")

        // Construct a Users object
        val user = Users(
            projectName,
            startDate,
            endDate,
            category,
            startTime,
            endTime,
            totalDuration,
            workDescription
        )

        if (uid != null) {
            databaseReference.child(uid).setValue(user).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // If data saving is successful, upload the image
                    imageUri?.let { uploadImage(it) }
                    Toast.makeText(this@TimesheetEntry, "Data Successfully Saved", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@TimesheetEntry, "Failed To Save Timesheet Entry", Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICKER_REQUEST_CODE)
    }

    private fun uploadImage(imageUri: Uri) {
        // Storage reference to the root path where images will be stored
        val storageReference = FirebaseStorage.getInstance().reference
        // Create a reference to the location where the image will be stored
        val imageRef =
            storageReference.child("images/${auth.currentUser?.uid}/${UUID.randomUUID()}")

        // Upload the image to Firebase Storage
        imageRef.putFile(imageUri)
            .addOnSuccessListener {
                Toast.makeText(this@TimesheetEntry, "Image Successfully Uploaded", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this@TimesheetEntry, "Failed To Upload Image", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            // Handle the selected image URI here
            val selectedImageUri: Uri? = data?.data
            if (selectedImageUri != null) {
                // Save the selected image URI
                imageUri = selectedImageUri
            }
        }
    }

    data class Users(
        var projectName: String? = null,
        var date: String? = null,
        var category: String? = null,
        var startDate: String? = null,
        var endDate: String? = null,
        var startTime: String? = null,
        var endTime: String? = null,
        var totalDuration: String? = null,
        var workDescriptionInput: String? = null
    )
}
