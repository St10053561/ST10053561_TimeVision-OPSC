package com.example.timevision_application

import android.Manifest
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.util.*

class TimesheetEntry : AppCompatActivity() {

    companion object {
        private const val IMAGE_PICKER_REQUEST_CODE = 100
        private const val CAMERA_REQUEST_CODE = 101
        private const val CAMERA_PERMISSION_REQUEST_CODE = 102
        private const val WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 103
    }

    private lateinit var browseFilesButton: ImageView
    private lateinit var projectNameInput: TextInputEditText
    private lateinit var dateInput: TextInputEditText
    private lateinit var categoryInput: Spinner
    private lateinit var startTimeInput: TextInputEditText
    private lateinit var endTimeInput: TextInputEditText
    private lateinit var minimumDailyHoursInput: EditText
    private lateinit var maximumDailyHoursInput: EditText
    private lateinit var totalDuration: TextInputEditText
    private lateinit var workDescriptionInput: TextInputEditText
    private lateinit var submitButton: Button

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: FirebaseStorage
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timesheet_entry)

        storageReference = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference

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

        dateInput.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) showDatePicker(dateInput)
        }

        startTimeInput.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) showTimePicker(startTimeInput)
        }

        endTimeInput.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) showTimePicker(endTimeInput)
        }

        browseFilesButton.setOnClickListener {
            openImagePicker()
        }

        submitButton.setOnClickListener {
            val projectName = projectNameInput.text.toString()
            val date = dateInput.text.toString()
            val category = categoryInput.selectedItem.toString()
            val startTime = startTimeInput.text.toString()
            val endTime = endTimeInput.text.toString()
            val minimumDailyHours = minimumDailyHoursInput.text.toString()
            val maximumDailyHours = maximumDailyHoursInput.text.toString()
            val totalDuration = totalDuration.text.toString()
            val workDescription = workDescriptionInput.text.toString()

            when {
                projectName.isEmpty() -> Toast.makeText(
                    this,
                    "Please fill in the Project Name",
                    Toast.LENGTH_SHORT
                ).show()

                startTime.isEmpty() -> Toast.makeText(
                    this,
                    "Please Pick The Start Time",
                    Toast.LENGTH_SHORT
                ).show()

                category.isEmpty() -> Toast.makeText(
                    this,
                    "Please fill in the Category",
                    Toast.LENGTH_SHORT
                ).show()

                startTime.isEmpty() -> Toast.makeText(
                    this,
                    "Please Pick The Start Time",
                    Toast.LENGTH_SHORT
                ).show()

                endTime.isEmpty() -> Toast.makeText(
                    this,
                    "Please Pick The End Time",
                    Toast.LENGTH_SHORT
                ).show()

                minimumDailyHours.isEmpty() -> Toast.makeText(
                    this,
                    "Please Pick the Minimum Daily Hours",
                    Toast.LENGTH_SHORT
                ).show()

                maximumDailyHours.isEmpty() -> Toast.makeText(
                    this,
                    "Please Pick the Maximum Daily Hours",
                    Toast.LENGTH_SHORT
                ).show()

                totalDuration.isEmpty() -> Toast.makeText(
                    this,
                    "Please fill in the Total Duration",
                    Toast.LENGTH_SHORT
                ).show()

                workDescription.isEmpty() -> Toast.makeText(
                    this,
                    "Please fill in the Work Description",
                    Toast.LENGTH_SHORT
                ).show()

                else -> saveToDatabase(
                    projectName,
                    date,
                    category,
                    startTime,
                    endTime,
                    minimumDailyHours,
                    maximumDailyHours,
                    totalDuration,
                    workDescription,
                    imageUri
                )
            }
        }

        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
        val database = FirebaseDatabase.getInstance().getReference("Categories")
        database.child(currentUserId!!).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val categories = mutableListOf<String>()
                for (categorySnapshot in dataSnapshot.children) {
                    val category = categorySnapshot.getValue(Category::class.java)
                    if (category != null) {
                        categories.add(category.name)
                    }
                }
                val adapter = ArrayAdapter(
                    this@TimesheetEntry,
                    android.R.layout.simple_spinner_item,
                    categories
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                categoryInput.adapter = adapter
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        })
    }

    private fun showDatePicker(textInputEditText: TextInputEditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog =
            DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = "${selectedMonth + 1}/$selectedDay/$selectedYear"
                textInputEditText.setText(selectedDate)
            }, year, month, day)

        datePickerDialog.show()
    }

    private fun showTimePicker(editText: EditText) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
            val selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
            editText.setText(selectedTime)
        }, hour, minute, true)

        timePickerDialog.show()
    }

    private fun openImagePicker() {
        AlertDialog.Builder(this)
            .setTitle("Select Action")
            .setItems(arrayOf("Take Photo", "Choose from Gallery")) { _, which ->
                when (which) {
                    0 -> openCamera()
                    1 -> openGallery()
                }
            }
            .show()
    }

    private fun openCamera() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST_CODE
            )
        } else {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent.resolveActivity(packageManager) != null) {
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE)
            }
        }
    }

    private fun openGallery() {
        val pickPhotoIntent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickPhotoIntent.type = "image/*"
        startActivityForResult(pickPhotoIntent, IMAGE_PICKER_REQUEST_CODE)
    }

    private fun uploadImage(imageUri: Uri, onSuccess: (String) -> Unit) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Uploading...")
        progressDialog.show()

        val imageRef =
            storageReference.reference.child("images/${auth.currentUser?.uid}/${UUID.randomUUID()}")

        val uploadTask = imageRef.putFile(imageUri)
        uploadTask.addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                progressDialog.dismiss()
                onSuccess(uri.toString()) // Pass the image URL to the callback
                Toast.makeText(
                    this@TimesheetEntry,
                    "Image Successfully Uploaded",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }.addOnFailureListener {
            progressDialog.dismiss()
            Toast.makeText(this@TimesheetEntry, "Failed To Upload Image", Toast.LENGTH_SHORT).show()
        }.addOnProgressListener { taskSnapshot ->
            val progress = 100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
            progressDialog.setMessage("Uploaded ${progress.toInt()}%")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                IMAGE_PICKER_REQUEST_CODE -> {
                    val selectedImageUri: Uri? = data?.data
                    if (selectedImageUri != null) {
                        imageUri = selectedImageUri
                        browseFilesButton.setImageURI(selectedImageUri)
                    }
                }

                CAMERA_REQUEST_CODE -> {
                    val photo: Bitmap = data?.extras?.get("data") as Bitmap
                    imageUri = getImageUri(photo)
                    browseFilesButton.setImageBitmap(photo)
                }
            }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_PERMISSION_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    openCamera()
                } else {
                    Toast.makeText(
                        this,
                        "Camera permission is required to take pictures",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

                } else {
                    Toast.makeText(
                        this,
                        "Storage permission is required to save images",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun getImageUri(inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

    private fun saveToDatabase(
        projectName: String,
        date: String,
        category: String,
        startTime: String,
        endTime: String,
        minimumDailyHours: String,
        maximumDailyHours: String,
        totalDuration: String,
        workDescription: String,
        imageUri: Uri?
    ) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Saving...")
        progressDialog.show()

        val timesheetEntryData = TimesheetEntryData(
            projectName, date, category, startTime, endTime,
            minimumDailyHours, maximumDailyHours, totalDuration, workDescription
        )

        val userId = auth.currentUser?.uid ?: return
        val timesheetEntryRef = databaseReference.child("TimeSheetEntries").child(userId).push()

        if (imageUri != null) {
            uploadImage(imageUri) { imageUrl ->
                timesheetEntryData.imageUrl = imageUrl
                timesheetEntryRef.setValue(timesheetEntryData).addOnCompleteListener { task ->
                    progressDialog.dismiss()
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this@TimesheetEntry,
                            "Timesheet Entry Saved",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    } else {
                        Toast.makeText(
                            this@TimesheetEntry,
                            "Failed to Save Timesheet Entry",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        } else {
            timesheetEntryRef.setValue(timesheetEntryData).addOnCompleteListener { task ->
                progressDialog.dismiss()
                if (task.isSuccessful) {
                    Toast.makeText(this@TimesheetEntry, "Timesheet Entry Saved", Toast.LENGTH_SHORT)
                        .show()
                    finish()
                } else {
                    Toast.makeText(
                        this@TimesheetEntry,
                        "Failed to Save Timesheet Entry",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }


    data class TimesheetEntryData(
        var projectName: String? = null,
        var date: String? = null,
        var category: String? = null,
        var startTime: String? = null,
        var endTime: String? = null,
        var minimumDailyHours: String? = null,
        var maximumDailyHours: String? = null,
        var totalDuration: String? = null,
        var workDescription: String? = null,
        var imageUrl: String? = null
    )
}
