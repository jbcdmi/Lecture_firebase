package com.example.lecture_firebase

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.exceptions.ClearCredentialException
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.lecture_firebase.Auth.LoginPage
import com.example.lecture_firebase.RealTimeDatabase.RealtimeLecture
import com.example.lecture_firebase.ui.theme.Lecture_firebaseTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalGlideComposeApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val currentUser = Firebase.auth.currentUser

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
                        Text("Home Page")
                        GlideImage(
                            model = currentUser?.photoUrl,
                            contentDescription = null,
                            modifier = Modifier.size(100.dp)
                        )
                        Text(currentUser?.displayName ?: "NONE")
                        Text(currentUser?.phoneNumber ?: "NONE")
                        Text(currentUser?.email ?: "NONE")
                        Button(onClick = {
                            logoutUser()

                        }) {
                            Text("logout")
                        }
                        Spacer(modifier = Modifier.height(50.dp))
                        Button(onClick = {
                           startActivity(Intent(this@MainActivity, RealtimeLecture::class.java))
                        }) {
                            Text("Realtime Database")
                        }
                    }
                }
            }
        }
    }

    fun logoutUser()
    {
        Firebase.auth.signOut()
        // When a user signs out, clear the current user credential state from all credential providers.
        lifecycleScope.launch {
            try {
                val credentialManager: CredentialManager = CredentialManager.create(this@MainActivity)
                val clearRequest = ClearCredentialStateRequest()
                credentialManager.clearCredentialState(clearRequest)
            } catch (e: ClearCredentialException) {
                Log.e("=====", "Couldn't clear user credentials: ${e.localizedMessage}")
            }
        }

        val intent = Intent(this@MainActivity, LoginPage::class.java)
        startActivity(intent)
        finish()
    }

}
