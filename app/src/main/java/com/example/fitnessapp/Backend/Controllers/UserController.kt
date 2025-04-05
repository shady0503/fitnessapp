package com.example.fitnessapp.Backend.Controllers

import com.example.fitnessapp.Backend.Models.Users
import com.example.fitnessapp.Backend.Repositories.UserRepository

class UserController(private val repository: UserRepository) {

    fun registerUser(user: Users):String{
        return if (repository.addUser(user)) "User registered successfully"
        else "User already exists"
    }

    fun getAllUsers(): List<Users> = repository.findAllUsers()

    fun getUserById(userId: String): Users? = repository.getUserById(userId)
}