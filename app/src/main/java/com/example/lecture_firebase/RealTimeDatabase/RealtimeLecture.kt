package com.example.lecture_firebase.RealTimeDatabase

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.lecture_firebase.RealTimeDatabase.ui.theme.Lecture_firebaseTheme
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import org.json.JSONObject

class RealtimeLecture : ComponentActivity() {

    var data by mutableStateOf("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

//        fetchRealtimeDataOnce()
        fetchRealtimeData()

        setContent {
            Lecture_firebaseTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(data)
                    }
                }
            }
        }
    }

    private fun fetchRealtimeDataOnce() {
        val database: DatabaseReference = Firebase.database.reference
        database.get().addOnSuccessListener {
            if (it.exists()) {
                val value = it.value
                Log.d("=====", "Value is: $value")
                data = value.toString()
                val jsonObject = JSONObject(value.toString())
            } else {
                Log.d("=====", "No data found")
            }
        }.addOnFailureListener {
            Log.d("=====", "Failed to read value: ${it.message}")
        }
    }

    private fun fetchRealtimeData() {
        val database: DatabaseReference = Firebase.database.reference
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val value = snapshot.value
                    Log.d("=====", "Value is: $value")
                    data = value.toString()
                } else {
                    Log.d("=====", "No data found")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("=====", "Failed to read value: ${error.message}")
            }
        })
    }
}
