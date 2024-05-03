package com.example.timevision_application

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnFocusChangeListener
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity(), OnFocusChangeListener {
    // Firebase
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDatabase: DatabaseReference

    // Declaring Input Fields
    private lateinit var usernameInput: TextInputEditText
    private lateinit var passwordInput: TextInputEditText
    lateinit var loginBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Firebase
        FirebaseApp.initializeApp(this)

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance()

        // Initialize Firebase Database
        mDatabase = FirebaseDatabase.getInstance().reference.child("users")

        // Initialize input fields
        usernameInput = findViewById(R.id.usernameInput)
        passwordInput = findViewById(R.id.passwordInput)

        val registerButton = findViewById<Button>(R.id.register_btn)
        registerButton.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }

        loginBtn = findViewById(R.id.login_btn)
        loginBtn.setOnClickListener {
            validateAndLogin()
        }

        // Set focus change listener
        usernameInput.onFocusChangeListener = this
        passwordInput.onFocusChangeListener = this
    }

    // Validate input fields and login
    private fun validateAndLogin() {
        val username = usernameInput.text.toString().trim()
        val password = passwordInput.text.toString().trim()

        if (validateUsername() && validatePassword()) {
            loginUser(username, password)
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
            errorMessage = "A Password is Required"
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
                    if (!hasFocus) {
                        validateUsername() // Call validation function when losing focus
                    }
                }

                R.id.passwordInput -> {
                    if (!hasFocus) {
                        validatePassword() // Call validation function when losing focus
                    }
                }
            }
        }
    }

    // Firebase login
    // Modify the loginUser function to pass the username to getUserKey
    private fun loginUser(username: String, password: String) {
        mAuth.signInWithEmailAndPassword(username, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = mAuth.currentUser
                    user?.let {
                        getUserKey(username) // Pass the username here
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(
                        this,
                        "Authentication failed. ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    // Modify the getUserKey function to accept the username as a parameter
    private fun getUserKey(username: String) {
        mDatabase.orderByChild("email").equalTo(username).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    // User found, retrieve the key (value)
                    for (snapshot in dataSnapshot.children) {
                        val userKey = snapshot.key
                        moveToHomeScreen(userKey)
                    }
                } else {
                    // User not found
                    Toast.makeText(applicationContext, "User Does Not Exists", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors here
            }
        })
    }

    // Move to HomeScreen activity and pass user key
    private fun moveToHomeScreen(userKey: String?) {
        val intent = Intent(this, HomeScreen::class.java)
        intent.putExtra("userKey", userKey)
        startActivity(intent)
        finish() // Optionally finish the current activity
    }
}