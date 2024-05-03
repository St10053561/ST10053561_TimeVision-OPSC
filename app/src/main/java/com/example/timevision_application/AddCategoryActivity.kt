package com.example.timevision_application


import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Locale

data class Category(var name: String = "")

private val categories = mutableListOf<Category>()
private lateinit var categoryAdapter: CategoryAdapter

class AddCategoryActivity : AppCompatActivity() {

    private var selectedColor: Int = Color.TRANSPARENT
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_category)

        // setting a category name
        val categoryNameEditText: EditText = findViewById(R.id.categoryNameEditText)
        val submitButton: Button = findViewById(R.id.submitButton)
        val categoryRecyclerView: RecyclerView = findViewById(R.id.categoryRecyclerView)

        categoryAdapter = CategoryAdapter(categories)
        categoryRecyclerView.layoutManager = LinearLayoutManager(this)
        categoryRecyclerView.adapter = categoryAdapter


        // Get the current user ID
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

        // Fetch categories from Firebase
        val database = FirebaseDatabase.getInstance().getReference("Categories")
        database.child(currentUserId!!).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                categories.clear()
                for (categorySnapshot in dataSnapshot.children) {
                    val category = categorySnapshot.getValue(Category::class.java)
                    if (category != null) {
                        categories.add(category)
                    }
                }
                categoryAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle possible errors.
            }
        })

        submitButton.setOnClickListener {
            val categoryName = categoryNameEditText.text.toString()
            if (categoryName.isNotBlank()) {
                val newCategory = Category(categoryName)
                // Save the new category to Firebase
                database.child(currentUserId).push().setValue(newCategory)
                categoryNameEditText.text.clear()
            }
        }

        val homeButton: Button = findViewById(R.id.homeButton)
        homeButton.setOnClickListener {
            // Start HomeActivity when homeButton is clicked
            startActivity(Intent(this, HomeScreen::class.java))
        }
    }
}