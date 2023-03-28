package com.example.week6

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var saveBtn: Button
    private lateinit var displayDataBtn: Button
    private lateinit var usernameField: EditText
    private lateinit var passwordField: EditText

    companion object {
        const val USERNAME_KEY = "username"
        const val PASSWORD_KEY = "password"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        saveBtn = findViewById(R.id.saveBtn)
        displayDataBtn = findViewById(R.id.displayDataBtn)
        usernameField = findViewById(R.id.username)
        passwordField = findViewById(R.id.password)

        val sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE)


        saveBtn.setOnClickListener {
            val sharedPreferencesEditor = sharedPreferences.edit()

            sharedPreferencesEditor.putString(USERNAME_KEY, usernameField.text.toString())
            sharedPreferencesEditor.putString(PASSWORD_KEY, passwordField.text.toString())
            sharedPreferencesEditor.apply()

            Toast.makeText(this, "Saved Successfully", Toast.LENGTH_SHORT)
                .show()
            val inputManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(it.windowToken, 0)
        }

        displayDataBtn.setOnClickListener {
            val username = sharedPreferences.getString(USERNAME_KEY, "")
            val password = sharedPreferences.getString(PASSWORD_KEY, "")

            usernameField.setText(username)
            passwordField.setText(password)
        }
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        outState.apply {
            putString(USERNAME_KEY, usernameField.text.toString())
            putString(PASSWORD_KEY, passwordField.text.toString())
        }
        super.onSaveInstanceState(outState, outPersistentState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        val username = savedInstanceState.getString(USERNAME_KEY)
        usernameField.setText(username ?: "")
        val password = savedInstanceState.getString(PASSWORD_KEY)
        passwordField.setText(password ?: "")

        super.onRestoreInstanceState(savedInstanceState)
    }


}