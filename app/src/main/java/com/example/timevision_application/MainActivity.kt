package com.example.timevision_application

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnFocusChangeListener
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class MainActivity : AppCompatActivity(), OnFocusChangeListener {
    //Declaring Input Fields
    private lateinit var usernameInput: TextInputEditText
    private lateinit var passwordInput: TextInputEditText
    lateinit var loginBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize input fields
        usernameInput = findViewById(R.id.usernameInput)
        passwordInput = findViewById(R.id.passwordInput)

        val registerButton = findViewById<Button>(R.id.register_btn)
        registerButton.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }

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
        }
        if (errorMessage != null) {
            // Access the TextInputLayout associated with nameInput
            val nameInputLayout = passwordInput.parent.parent as TextInputLayout
            nameInputLayout.isErrorEnabled = true
            nameInputLayout.error = errorMessage
        }
        return errorMessage == null
    }

    override fun onFocusChange(view: View, hasFocus: Boolean) {
        if (view != null) {
            when (view.id) {
                R.id.usernameInput -> {
                    if (hasFocus) {
                        // Checking if error is enabled and removing it when focused
                        val nameInputLayout = usernameInput.parent.parent as TextInputLayout
                        if (nameInputLayout.isErrorEnabled) {
                            nameInputLayout.isErrorEnabled = false
                        }
                    } else {
                        validateUsername() // Call validation function when losing focus
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
                        validatePassword() // Call validation function when losing focus
                    }
                }
            }
        }
    }
}