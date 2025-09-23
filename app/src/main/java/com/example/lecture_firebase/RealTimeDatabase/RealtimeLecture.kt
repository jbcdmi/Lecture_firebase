package com.example.lecture_firebase.RealTimeDatabase

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.lecture_firebase.RealTimeDatabase.ui.theme.Lecture_firebaseTheme
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import org.json.JSONObject

class RealtimeLecture : ComponentActivity() {

    var data by mutableStateOf("no data")
    var isShowDialog by mutableStateOf(false)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

//        fetchDataOnce()
        fetchDataRealtime()
        setContent {
            Lecture_firebaseTheme {
                Scaffold(modifier = Modifier.fillMaxSize(), floatingActionButton = {
                    FloatingActionButton(onClick = {
                        isShowDialog = true
                    }) {
                        Icon(Icons.Default.Add, contentDescription = "Add")
                    }
                }) { innerPadding ->

                    if (isShowDialog) showAddDialog()

                    Box(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                    ) {
                        Text(data, modifier = Modifier.align(Alignment.Center))
                    }
                }
            }
        }
    }

    @Composable
    private fun showAddDialog() {
        var name by remember { mutableStateOf("") }
        var phone by remember { mutableStateOf("") }
        Dialog(onDismissRequest = {
            isShowDialog = false
        }) {
            Card {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Dialog")
                    Spacer(Modifier.padding(8.dp))
                    OutlinedTextField(name, onValueChange = {
                        name = it
                    }, label = {
                        Text("Name")
                    })
                    Spacer(Modifier.padding(8.dp))
                    OutlinedTextField(phone, onValueChange = {
                        phone = it
                    }, label = {
                        Text("phone")
                    })
                    Spacer(Modifier.padding(16.dp))
                    FloatingActionButton(onClick = {
                        isShowDialog = false
                    }) {
                        Row(modifier = Modifier.padding(horizontal = 20.dp)) {
                            Icon(Icons.Default.Add, contentDescription = "Add")
                            Text(" add")
                        }
                    }
                }

            }
        }
    }

    fun fetchDataRealtime() {
        var database = Firebase.database.reference
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                data = dataSnapshot?.value?.toString() ?: "Value no found";

                val dataMap = dataSnapshot.value as? HashMap<String, Any>
                Log.d("=====", "onDataChange: limit = ${dataMap?.get("limit")}")
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("=====", "loadPost:onCancelled", databaseError.toException())
                data = databaseError.message
            }
        }
        database.addValueEventListener(postListener)
    }

    fun fetchDataOnce() {
        var database = Firebase.database.reference
        database.get().addOnSuccessListener {
            data = it?.value?.toString() ?: "Value no found";
        }.addOnFailureListener {
            data = it.localizedMessage.toString()
        }
    }

}
