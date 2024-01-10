package com.codersee.jwttokens.repository

import com.codersee.jwttokens.model.Role
import com.codersee.jwttokens.model.User
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class UserRepository(private val encoder: PasswordEncoder) {

    private val users = mutableListOf(
        User(
            id = UUID.randomUUID(),
            email = "shivani@inexture.com",
            password = encoder.encode("shivani@123"),
            role = Role.USER
           ),

        User(
            id = UUID.randomUUID(),
            email = "jemy@inexture.com",
            password =encoder.encode("jemy@890"),
            role = Role.USER
        ),
        User(
            id = UUID.randomUUID(),
            email = "riddhi@inexture.com",
            password = encoder.encode("r@1234"),
            role = Role.ADMIN
        ),
    )


    fun save(user: User): Boolean {
        val updated = user.copy(password = encoder.encode(user.password))
        return users.add(updated)
    }


    fun findByEmail(email: String): User? =
        users.firstOrNull { it.email == email }

    fun findAll(): List<User> = users

    fun findByUUID(uuid: UUID): User? = users.firstOrNull { it.id == uuid }

    fun deleteByUUID(uuid: UUID): Boolean{
        val foundUser = findByUUID(uuid)

        return foundUser?.let {
            users.remove(it)
        }?: false
    }

}
