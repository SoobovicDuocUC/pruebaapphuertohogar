package com.example.projectohuertoapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController

class HomeViewModel : ViewModel() {
    fun onLoginIconClicked(navController: NavController) {
        navController.navigate("login")
    }
}