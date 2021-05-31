package com.hungry.oauthsample.infrastructure

import com.hungry.oauthsample.domain.User
import com.hungry.oauthsample.domain.UserRepository
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Column

internal object User: LongIdTable() {
    val email: Column<String> = varchar("email", 200)
    val password: Column<String> = varchar("password", 200)
    val phoneNumber: Column<String> = varchar("phoneNumber", 100)
    val name: Column<String?> = varchar("name", 200).nullable()
    val age: Column<Int?> = integer("age").nullable()
}

class SqlUserRepository: UserRepository {
    override fun saveUser(user: User, hashedPassword: String, salt: String) {
        TODO("Not yet implemented")
    }


}