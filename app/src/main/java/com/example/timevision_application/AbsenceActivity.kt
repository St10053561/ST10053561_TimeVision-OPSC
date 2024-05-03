package com.example.timevision_application

import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.database

class AbsenceActivity  : AppCompatActivity(){
    private lateinit var startDatePicker: DatePicker
    private lateinit var endDatePicker: DatePicker
    private lateinit var reasonEditText: EditText
    private lateinit var commentsEditText: EditText
    private val db = Firebase.database.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_absence)

        // Initialize views
        startDatePicker = findViewById(R.id.startDatePicker)
        endDatePicker = findViewById(R.id.endDatePicker)
        reasonEditText = findViewById(R.id.reasonEditText)
        commentsEditText = findViewById(R.id.commentsEditText)

        val submitButton = findViewById<Button>(R.id.submitButton)

        submitButton.setOnClickListener {
            // Retrieve values from views
            val startDate =
                "${startDatePicker.month + 1}-${startDatePicker.dayOfMonth}-${startDatePicker.year}"
            val endDate =
                "${endDatePicker.month + 1}-${endDatePicker.dayOfMonth}-${endDatePicker.year}"
            val reason = reasonEditText.text.toString()
            val comments = commentsEditText.text.toString()

            // Save information
            saveInformation(startDate, endDate, reason, comments)

            // Clear form fields
            clearForm()
        }
    }


    private fun saveInformation(startDate: String, endDate: String, reason: String, comments: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val absenceData = hashMapOf(
            "startDate" to startDate,
            "endDate" to endDate,
            "reason" to reason,
            "comments" to comments
        )

        db.child("absences").child(userId ?: "null").push().setValue(absenceData)
            .addOnSuccessListener {
                showToast("Absence request saved.")
            }
            .addOnFailureListener { e ->
                showToast("Error adding document")
            }
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