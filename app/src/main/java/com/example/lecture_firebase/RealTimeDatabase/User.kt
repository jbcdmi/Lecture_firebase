package com.example.lecture_firebase.RealTimeDatabase

import com.google.firebase.database.Exclude

data class User(val name: String, val phone: String) {

    @Exclude
    var key: String? = null

        constructor() : this("", "")

}
