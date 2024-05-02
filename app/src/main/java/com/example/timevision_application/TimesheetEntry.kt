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
    private lateinit var minimumDailyHoursInput: EditText
    private lateinit var maximumDailyHoursInput: EditText
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
        minimumDailyHoursInput = findViewById(R.id.minimumDailyHoursInput)
        maximumDailyHoursInput = findViewById(R.id.maximumDailyHoursInput)
        workDescriptionInput = findViewById(R.id.workDescriptionInput)
        totalDuration = findViewById(R.id.totalDuration)
        submitButton = findViewById(R.id.submitButton)
        browseFilesButton = findViewById(R.id.browseFilesButton)
        // make a cmment

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

        // Set OnClickListener to open TimePickerDialog for minimumDailyHoursInput
        minimumDailyHoursInput.setOnClickListener {
            showTimePicker(minimumDailyHoursInput)
        }

        // Set OnClickListener to open TimePickerDialog for maximumDailyHoursInput
        maximumDailyHoursInput.setOnClickListener {
            showTimePicker(maximumDailyHoursInput)
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
            val minimumDailyHours = minimumDailyHoursInput.text.toString()
            val maximumDailyHours = maximumDailyHoursInput.text.toString()
            val totalDuration = totalDuration.text.toString()

            saveToDatabase(projectName, category, workDescription, startDate, endDate, minimumDailyHours, maximumDailyHours,totalDuration, imageUri)
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

    private fun saveToDatabase(
        projectName: String,
        category: String,
        workDescription: String,
        startDate: String,
        endDate: String,
        minimumDailyHours: String,
        maximumDailyHours: String,
        totalDuration: String,
        // Pass the image URI as a parameter
        imageUri: Uri?
    ) {
        // Code to save entry to a database
        val uid = auth.currentUser?.uid
        databaseReference = FirebaseDatabase.getInstance().getReference("TimeSheetEntries")

        // Construct a TimesheetEntry object
        val entry = TimesheetEntry(
            projectName,
            startDate,
            endDate,
            category,
            minimumDailyHours,
            maximumDailyHours,
            workDescription,
            totalDuration
        )

        if (uid != null) {
            databaseReference.child(uid).setValue(entry).addOnCompleteListener { task ->
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

    data class TimesheetEntry(
        var projectName: String? = null,
        var startDate: String? = null,
        var endDate: String? = null,
        var category: String? = null,
        var minimumDailyHours: String? = null,
        var maximumDailyHours: String? = null,
        var workDescription: String? = null,
        var totalDuration: String? = null
    )
}
