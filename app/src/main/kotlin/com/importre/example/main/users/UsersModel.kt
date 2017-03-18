package com.importre.example.main.users

import com.importre.example.model.User

data class UsersModel(
    val user: User = User(0, "", ""),
    val loading: Boolean = false,

    val nameSize: Float = 24f,
    val emailSize: Float = 24f
)
