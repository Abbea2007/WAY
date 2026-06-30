package com.example.wayapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.wayapp.R

class UserProfileViewModel : ViewModel() {
    var name by mutableStateOf("Username")
    var role by mutableStateOf("Estudiante")
    var major by mutableStateOf("Ingeniería de Software")
    var profilePhotoRes by mutableIntStateOf(R.drawable.img)
    val studentId = "ID: 20231345"

    fun updateProfile(newName: String, newRole: String, newMajor: String) {
        name = newName
        role = newRole
        major = newMajor
    }
}
