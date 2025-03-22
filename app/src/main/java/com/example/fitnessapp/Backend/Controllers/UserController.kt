package com.example.fitnessapp.Backend.Controllers

import com.example.fitnessapp.Backend.Models.User
import com.example.fitnessapp.Backend.Repositories.UserRepository

class UserController(private val repository: UserRepository) {

    fun registerUser(user: User):String{
        return if (repository.addUser(user)) "User registered successfully"
        else "User already exists"
    }

    fun getAllUsers(): List<User> = repository.findAllUsers()

    fun getUserById(userId: String): User? = repository.getUserById(userId)
}