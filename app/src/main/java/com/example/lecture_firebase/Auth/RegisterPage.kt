package com.example.lecture_firebase.Auth

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.lecture_firebase.ui.theme.Lecture_firebaseTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class RegisterPage : ComponentActivity() {
    var email by mutableStateOf("")
    var password by mutableStateOf("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lecture_firebaseTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Register")
                        Spacer(modifier = Modifier.height(20.dp))
                        OutlinedTextField(email, onValueChange = {
                            email = it
                        }, label = {
                            Text("enter email...")
                        })
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(password, onValueChange = {
                            password = it
                        }, label = {
                            Text("enter password...")
                        })
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = {
                            // add validation
                            if (email.isEmpty()) {
                                Toast.makeText(
                                    this@RegisterPage,
                                    "please enter email",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@Button
                            }
                            registerUser()
                        }) {
                            Text("Signup")
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        TextButton(onClick = {
                            finish()
                        }) {
                            Text("Login")
                        }
                    }
                }
            }
        }
    }

    fun registerUser() {
        val auth: FirebaseAuth = Firebase.auth
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("=====", "createUserWithEmail:success")
                    val user = auth.currentUser
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("=====", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }

}
