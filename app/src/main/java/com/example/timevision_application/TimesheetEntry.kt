package com.example.timevision_application

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream
import java.util.*

class TimesheetEntry : AppCompatActivity() {

    // Companion object for constants
    companion object {
        private const val IMAGE_PICKER_REQUEST_CODE = 100
        private const val CAMERA_REQUEST_CODE = 101
        private const val CAMERA_PERMISSION_REQUEST_CODE = 102
        private const val WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 103
    }

    // Global Variables
    private lateinit var browseFilesButton: ImageButton
    private lateinit var projectNameInput: TextInputEditText
    private lateinit var dateInput: TextInputEditText
    private lateinit var categoryInput: TextInputEditText
    private lateinit var startTimeInput: TextInputEditText
    private lateinit var endTimeInput: TextInputEditText
    private lateinit var minimumDailyHoursInput: EditText
    private lateinit var maximumDailyHoursInput: EditText
    private lateinit var totalDuration: TextInputEditText
    private lateinit var workDescriptionInput: TextInputEditText
    private lateinit var submitButton: Button

    // Variables for database
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
        startTimeInput = findViewById(R.id.startTimeInput)
        endTimeInput = findViewById(R.id.endTimeInput)
        minimumDailyHoursInput = findViewById(R.id.minimumDailyHoursInput)
        maximumDailyHoursInput = findViewById(R.id.maximumDailyHoursInput)
        workDescriptionInput = findViewById(R.id.workDescriptionInput)
        totalDuration = findViewById(R.id.totalDuration)
        submitButton = findViewById(R.id.submitButton)
        browseFilesButton = findViewById(R.id.browseFilesButton)

        // Set OnClickListener to open DatePickerDialog for dateInput
        dateInput.setOnClickListener {
            showDatePicker(dateInput)
        }

        // Set OnClickListener to open DatePickerDialog for startDateInput
        startTimeInput.setOnClickListener {
            showTimePicker(startTimeInput)
        }

        // Set OnClickListener to open DatePickerDialog for endDateInput
        endTimeInput.setOnClickListener {
            showTimePicker(endTimeInput)
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
            val startTime = startTimeInput.text.toString()
            val endTime = endTimeInput.text.toString()
            val minimumDailyHours = minimumDailyHoursInput.text.toString()
            val maximumDailyHours = maximumDailyHoursInput.text.toString()
            val totalDuration = totalDuration.text.toString()

            // Check if any of the fields are empty
            when {
                projectName.isEmpty() -> Toast.makeText(this, "Please fill in the Project Name", Toast.LENGTH_SHORT).show()
                category.isEmpty() -> Toast.makeText(this, "Please fill in the Category", Toast.LENGTH_SHORT).show()
                workDescription.isEmpty() -> Toast.makeText(this, "Please fill in the Work Description", Toast.LENGTH_SHORT).show()
                startTime.isEmpty() -> Toast.makeText(this, "Please Pick The Start Time", Toast.LENGTH_SHORT).show()
                endTime.isEmpty() -> Toast.makeText(this, "Please Pick The End Time", Toast.LENGTH_SHORT).show()
                minimumDailyHours.isEmpty() -> Toast.makeText(this, "Please Pick the Minimum Daily Hours", Toast.LENGTH_SHORT).show()
                maximumDailyHours.isEmpty() -> Toast.makeText(this, "Please Pick the Maximum Daily Hours", Toast.LENGTH_SHORT).show()
                totalDuration.isEmpty() -> Toast.makeText(this, "Please fill in the Total Duration", Toast.LENGTH_SHORT).show()
                else -> saveToDatabase(projectName, category, workDescription, startTime, endTime, minimumDailyHours, maximumDailyHours,totalDuration, imageUri)
            }
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
        startTime: String,
        endTime: String,
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
        val entry = TimesheetEntryData(
            projectName,
            startTime,
            endTime,
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
                    imageUri?.let { uploadImage(it) {
                        // Update browseFilesButton after successful upload
                        browseFilesButton.setImageResource(R.drawable.ic_image_uploaded)
                    } }
                    Toast.makeText(this@TimesheetEntry, "Data Successfully Saved", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@TimesheetEntry, "Failed To Save Timesheet Entry", Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }


    private fun openImagePicker() {
        AlertDialog.Builder(this)
            .setTitle("Select Action")
            .setItems(arrayOf("Take Photo", "Choose from Gallery")) { _, which ->
                when (which) {
                    0 -> openCamera() // Take Photo
                    1 -> openGallery() // Choose from Gallery
                }
            }
            .show()
    }

    private fun openCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE)
        } else {
            // Permission is granted, open the camera
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent.resolveActivity(packageManager) != null) {
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE)
            }
        }
    }

    private fun openGallery() {
        val pickPhotoIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(pickPhotoIntent, IMAGE_PICKER_REQUEST_CODE)
    }

    private fun uploadImage(imageUri: Uri, onSuccess: () -> Unit) {
        // Storage reference to the root path where images will be stored
        val storageReference = FirebaseStorage.getInstance().reference
        // Create a reference to the location where the image will be stored
        val imageRef =
            storageReference.child("images/${auth.currentUser?.uid}/${UUID.randomUUID()}")

        // Upload the image to Firebase Storage
        imageRef.putFile(imageUri)
            .addOnSuccessListener {
                onSuccess()
                Toast.makeText(this@TimesheetEntry, "Image Successfully Uploaded", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this@TimesheetEntry, "Failed To Upload Image", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                IMAGE_PICKER_REQUEST_CODE -> {
                    // Handle the selected image URI here
                    val selectedImageUri: Uri? = data?.data
                    if (selectedImageUri != null) {
                        // Save the selected image URI
                        imageUri = selectedImageUri
                    }
                }
                CAMERA_REQUEST_CODE -> {
                    val photo: Bitmap = data?.extras?.get("data") as Bitmap
                    // Check for WRITE_EXTERNAL_STORAGE permission before converting bitmap to Uri
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        // Permission is not granted, request it
                        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE)
                    } else {
                        // Permission is granted, proceed with getting the image URI
                        imageUri = getImageUri(photo)
                    }
                }
            }
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_PERMISSION_REQUEST_CODE -> {
                // existing code...
            }
            WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Permission was granted, you can now write to external storage
                } else {
                    // Permission was denied. You can show a message to the user explaining why the permission is necessary.
                    Toast.makeText(this, "Storage permission is required to save images", Toast.LENGTH_SHORT).show()
                }
                return
            }
            // Handle other permission results
        }
    }

    private fun getImageUri(inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

    data class TimesheetEntryData(
        var projectName: String? = null,
        var startTime: String? = null,
        var endTime: String? = null,
        var category: String? = null,
        var minimumDailyHours: String? = null,
        var maximumDailyHours: String? = null,
        var workDescription: String? = null,
        var totalDuration: String? = null
    )
}
