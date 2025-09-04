package com.example.lecture_firebase.RealTimeDatabase

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import org.json.JSONObject

class RealtimeLecture : ComponentActivity() {

    var list = mutableStateListOf<User>()
    var isShowDialog by mutableStateOf(false)

    var editData: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

//        fetchRealtimeDataOnce()
        fetchRealtimeData()

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

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        items(list.size) { index ->
                            val user = list[index]
                            Card(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .fillMaxSize()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .fillMaxSize(),
                                    horizontalArrangement = Arrangement.Start,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .weight(1f)
                                    ) {
                                        Text(user.name)
                                        Text(user.phone)
                                    }
                                    IconButton(onClick = {
                                        editData = user
                                        isShowDialog = true
                                    }) {
                                        Icon(Icons.Default.Edit, contentDescription = null)
                                    }
                                    IconButton(onClick = {
                                        deleteUser(user)
                                    }) {
                                        Icon(Icons.Default.Delete, contentDescription = null)
                                    }
                                }
                            }
                        }
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
                        editData?.let {
                            editUser(name, phone, it.key!!)
                        } ?: run {
                            addUser(name, phone)
                        }
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

    private fun editUser(name: String, phone: String, key: String) {
        val database: DatabaseReference = Firebase.database.reference
        val map = mapOf(
            "name" to name,
            "phone" to phone
        )
        database.child("users").child(key).updateChildren(map)
    }

    private fun deleteUser(user: User) {
        val database: DatabaseReference = Firebase.database.reference
        database.child("users").child(user.key!!).removeValue()
    }

    private fun addUser(name: String, phone: String) {
        val database: DatabaseReference = Firebase.database.reference
//        val map = mapOf(
//            "name" to name,
//            "phone" to phone
//        )
        val user = User(name, phone)
        database.child("users").push().setValue(user)
    }

    /*private fun fetchRealtimeDataOnce() {
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
    }*/

    private fun fetchRealtimeData() {
        val database: DatabaseReference = Firebase.database.reference
        database.child("users").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    list.clear()
                    // way-1
                    snapshot.children.forEach {
                        val user = it.getValue(User::class.java)
                        user?.key = it.key
                        Log.d("=====", "User: $user")
                        user?.let { u -> list.add(u) }
                    }

                    // way-2

//                    val value = snapshot.value
//                    val mainData = JSONObject(value.toString())
//                    Log.d("=====", "Value is: $value")
//                    for (key in mainData.keys()) {
//                        val userJson = mainData.getJSONObject(key)
//                        val user = User(
//                            name = userJson.getString("name"),
//                            phone = userJson.getString("phone")
//                        )
//                        user.key = key
//                        Log.d("=====", "User: $user")
//                        list.add(user)
//                    }
//                    data = value.toString()
                } else {
                    Log.d("=====", "No data found")
//                    data = "No data found"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("=====", "Failed to read value: ${error.message}")
            }
        })
    }
}
