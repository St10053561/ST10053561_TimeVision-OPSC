package com.example.timevision_application

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.timevision_application.R
import com.example.timevision_application.TimesheetEntry

class HomeScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)

        val timeSheetEntryButton = findViewById<ImageButton>(R.id.timeSheetEntryButton)
        timeSheetEntryButton.setOnClickListener {
            // Start TimesheetEntry activity when timeSheetEntryButton is clicked
            startActivity(Intent(this@HomeScreen, TimesheetEntry::class.java))
        }
    }
}
