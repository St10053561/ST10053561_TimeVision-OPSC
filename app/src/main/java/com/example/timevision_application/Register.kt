package com.example.timevision_application

import android.os.Bundle
import android.util.Patterns
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.*

class Register : AppCompatActivity(), View.OnClickListener, View.OnFocusChangeListener, View.OnKeyListener {
    //Declaring Input Fields
    private lateinit var nameInput: TextInputEditText
    private lateinit var dateOfBirthInput: TextInputEditText
    private lateinit var surnameInput: TextInputEditText
    private lateinit var emailInput: TextInputEditText
    private lateinit var usernameInput: TextInputEditText
    private lateinit var passwordInput: TextInputEditText
    private lateinit var confirmPasswordInput: TextInputEditText

    //A variable to store the entered password
    lateinit var storePassword: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        // Initialize input fields
        nameInput = findViewById(R.id.nameInput)
        dateOfBirthInput = findViewById(R.id.dateOfBirthInput)
        surnameInput = findViewById(R.id.surnameInput)
        emailInput = findViewById(R.id.emailInput)
        usernameInput = findViewById(R.id.usernameInput)
        passwordInput = findViewById(R.id.passwordInput)
        confirmPasswordInput = findViewById(R.id.confirmpasswordInput)

        // Set up onFocusChangeListener for input fields
        nameInput.onFocusChangeListener = this
        surnameInput.onFocusChangeListener = this
        dateOfBirthInput.onFocusChangeListener = this
        emailInput.onFocusChangeListener = this
        usernameInput.onFocusChangeListener = this
        passwordInput.onFocusChangeListener = this
        confirmPasswordInput.onFocusChangeListener = this

        // Set onClickListener for the back button
        val backButton = findViewById<Button>(R.id.backButton)
        backButton.setOnClickListener {
            onBackPressed()
        }

        // Apply edge-to-edge display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.register_main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Set up click listener for dateOfBirthInput
        dateOfBirthInput.setOnClickListener {
            showDatePicker()
        }
    }
    //A method to show the DatePicker
    private fun showDatePicker() {
        val builder = MaterialDatePicker.Builder.datePicker()

        // Set the constraints to allow dates between 1900 and the current date (2024)
        val constraintsBuilder = CalendarConstraints.Builder()
        val minDate = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
            set(Calendar.YEAR, 1900)
        }.timeInMillis // Set minimum date to January 1, 1900
        val maxDate = MaterialDatePicker.todayInUtcMilliseconds() // Set maximum date to current date (2024)
        constraintsBuilder.setStart(minDate)
        constraintsBuilder.setEnd(maxDate)
        builder.setCalendarConstraints(constraintsBuilder.build())

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

    //A method to validate the Name
    private fun validateFullName(): Boolean {
        var errorMessage: String? = null
        val name: String = nameInput.text.toString()

        if (name.isNullOrEmpty()) {
            errorMessage = "Full Name Required"
        }
        if (errorMessage != null) {
            // Accessing the TextInputLayout associated with nameInput
            val nameInputLayout = nameInput.parent.parent as TextInputLayout
            nameInputLayout.isErrorEnabled = true
            nameInputLayout.error = errorMessage
        }
        return errorMessage == null
    }

    //A method to validate the Surname
    private fun validateSurname(): Boolean {
        var errorMessage: String? = null
        val surname: String = surnameInput.text.toString()

        if (surname.isNullOrEmpty()) {
            errorMessage = "Full Surname Required"
        }
        if (errorMessage != null) {
            // Access the TextInputLayout associated with nameInput
            val nameInputLayout = surnameInput.parent.parent as TextInputLayout
            nameInputLayout.isErrorEnabled = true
            nameInputLayout.error = errorMessage
        }
        return errorMessage == null
    }

    //A method to Check if the User Selected A Date
    private fun CheckForDate(): Boolean {
        var errorMessage: String? = null
        val datePresent: String = dateOfBirthInput.text.toString()

        if (datePresent.isNullOrEmpty()) {
            errorMessage = "Select Date Of Birth"
        }
        if (errorMessage != null) {
            // Access the TextInputLayout associated with nameInput
            val nameInputLayout = dateOfBirthInput.parent.parent as TextInputLayout
            nameInputLayout.isErrorEnabled = true
            nameInputLayout.error = errorMessage
        }
        return errorMessage == null
    }

    //A method to validate the Email
    private fun validateEmail(): Boolean {
        var errorMessage: String? = null
        val email: String = emailInput.text.toString()

        if (email.isNullOrEmpty()) {
            errorMessage = "Full Email is Required"
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            errorMessage = "Email Is InValid"
        }
        if (errorMessage != null) {
            // Access the TextInputLayout associated with nameInput
            val nameInputLayout = emailInput.parent.parent as TextInputLayout
            nameInputLayout.isErrorEnabled = true
            nameInputLayout.error = errorMessage
        }
        return errorMessage == null
    }

    //A method to validate the Username
    private fun validateUsername(): Boolean {
        var errorMessage: String? = null
        val username: String = usernameInput.text.toString()

        if (username.isNullOrEmpty()) {
            errorMessage = "A Username is Required"
        }
        if (errorMessage != null) {
            // Access the TextInputLayout associated with nameInput
            val nameInputLayout = usernameInput.parent.parent as TextInputLayout
            nameInputLayout.isErrorEnabled = true
            nameInputLayout.error = errorMessage
        }

        return errorMessage == null
    }

    //A method to validate the Password
    private fun validatePassword(): Boolean {
        var errorMessage: String? = null
        val password: String = passwordInput.text.toString()

        if (password.isNullOrEmpty()) {
            errorMessage += "A Password is Required"
        } else if (password.length < 8) {
            errorMessage += "The password must be a minimum of 8 characters long"
        } else if (!password.any { it.isUpperCase() }) {
            errorMessage += "The password must contain an uppercase letter"
        } else if (!password.any { it.isLowerCase() }) {
            errorMessage += "The password must contain a lowercase letter"
        } else if (!password.any { it.isDigit() }) {
            errorMessage += "The password must contain a digit"
        } else if (!password.any { !it.isLetterOrDigit() }) {
            errorMessage += "The password must contain a special character"
        }
        if (errorMessage != null) {
            // Access the TextInputLayout associated with nameInput
            val nameInputLayout = passwordInput.parent.parent as TextInputLayout
            nameInputLayout.isErrorEnabled = true
            nameInputLayout.error = errorMessage
        }
        storePassword = password
        return errorMessage == null
    }

    //A method to validate the Confirm Password
    private fun validateConfirmPassword(): Boolean {
        var errorMessage: String? = null
        val confirmPassword: String = confirmPasswordInput.text.toString()

        if (confirmPassword.isNullOrEmpty()) {
            errorMessage = "Password Confirmation is Required"
        }
        if (!confirmPassword.equals(storePassword)) {
            errorMessage = "Confirmation Password Does Not Match The Password Above"
        }
        if (errorMessage != null) {
            // Access the TextInputLayout associated with nameInput
            val nameInputLayout = confirmPasswordInput.parent.parent as TextInputLayout
            nameInputLayout.isErrorEnabled = true
            nameInputLayout.error = errorMessage
        }
        return errorMessage == null
    }

    override fun onFocusChange(view: View, hasFocus: Boolean) {
        if (view != null) {
            when (view.id) {
                R.id.nameInput -> {
                    if (hasFocus) {
                        // Checking if error is enabled and removing it when focused
                        val nameInputLayout = nameInput.parent.parent as TextInputLayout
                        if (nameInputLayout.isErrorEnabled) {
                            nameInputLayout.isErrorEnabled = false
                        }
                    } else {
                        validateFullName()
                    }
                }

                R.id.surnameInput -> {
                    if (hasFocus) {
                        // Checking if error is enabled and removing it when focused
                        val nameInputLayout = surnameInput.parent.parent as TextInputLayout
                        if (nameInputLayout.isErrorEnabled) {
                            nameInputLayout.isErrorEnabled = false
                        }
                    } else {
                        validateSurname()
                    }
                }

                R.id.dateOfBirthInput -> {
                    if (hasFocus) {
                        // Checking if error is enabled and removing it when focused
                        val nameInputLayout = dateOfBirthInput.parent.parent as TextInputLayout
                        if (nameInputLayout.isErrorEnabled) {
                            nameInputLayout.isErrorEnabled = false
                        }
                    } else {
                        CheckForDate()
                    }
                }

                R.id.emailInput -> {
                    if (hasFocus) {
                        // Checking if error is enabled and removing it when focused
                        val nameInputLayout = emailInput.parent.parent as TextInputLayout
                        if (nameInputLayout.isErrorEnabled) {
                            nameInputLayout.isErrorEnabled = false
                        }
                    } else {
                        validateEmail()
                    }
                }

                R.id.usernameInput -> {
                    if (hasFocus) {
                        // Checking if error is enabled and removing it when focused
                        val nameInputLayout = usernameInput.parent.parent as TextInputLayout
                        if (nameInputLayout.isErrorEnabled) {
                            nameInputLayout.isErrorEnabled = false
                        }
                    } else {
                        validateUsername()
                    }
                }

                R.id.passwordInput -> {
                    if (hasFocus) {
                        // Checking if error is enabled and removing it when focused
                        val nameInputLayout = passwordInput.parent.parent as TextInputLayout
                        if (nameInputLayout.isErrorEnabled) {
                            nameInputLayout.isErrorEnabled = false
                        }
                    } else {
                        validatePassword()
                    }
                }

                R.id.confirmpasswordInput -> {
                    if (hasFocus) {
                        // Checking if error is enabled and removing it when focused
                        val nameInputLayout =
                            confirmPasswordInput.parent.parent as TextInputLayout
                        if (nameInputLayout.isErrorEnabled) {
                            nameInputLayout.isErrorEnabled = false
                        }
                    } else {
                        validateConfirmPassword()
                    }
                }
            }
        }
    }

    override fun onClick(v: View?) {
    }

    override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
        return false
    }
}