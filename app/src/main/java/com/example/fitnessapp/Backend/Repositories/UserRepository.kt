package com.example.fitnessapp.Backend.Repositories

import com.example.fitnessapp.Backend.Models.User
import com.google.android.gms.common.internal.FallbackServiceBroker
import java.util.concurrent.ConcurrentHashMap

class UserRepository {
    private val users = ConcurrentHashMap<String, User>()

    /*object Users : Table() {
        val userId = varchar("userId", 50).primaryKey()
        val name = varchar("name", 100)
        val email = varchar("email", 100)
        val password = varchar("password", 100)
    }*/


    fun addUser(user: User) : Boolean {
        if (users.containsKey(user.userId)) return false

        users[user.userId] = user
        return true

            /*transaction {
                Users.insert {
                    it[userId] = user.userId
                    it[name] = user.name
                    it[email] = user.email
                    it[password] = hashedPassword
                }
            }*/
    }

    fun findAllUsers(): List<User> {
        return users.values.toList()
        /*return transaction {
            Users.selectAll().map { row ->
                User(
                    userId = row[Users.userId],
                    name = row[Users.name],
                    email = row[Users.email],
                    password = row[Users.password]
                )
            }
        }*/
    }

    fun getUserById(userId: String): User? = users[userId]
}