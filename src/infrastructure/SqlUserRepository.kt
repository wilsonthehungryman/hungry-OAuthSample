package com.hungry.oauthsample.infrastructure

import com.hungry.oauthsample.domain.users.User
import com.hungry.oauthsample.domain.users.UserRepository
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

internal object UserTable: LongIdTable() {
    val email: Column<String> = varchar("email", 200).uniqueIndex()
    val phoneNumber: Column<String> = varchar("phone_number", 100)
    val name: Column<String?> = varchar("name", 200).nullable()
    val age: Column<Int?> = integer("age").nullable()
}

internal object CredentialTable: Table() {
    val userId: Column<String> = varchar("user_id", 200).uniqueIndex()
    val hashedPassword: Column<String> = varchar("hashed_password", 200)
}

class SqlUserRepository: UserRepository {
    init {
        transaction {
            SchemaUtils.create(UserTable)
            SchemaUtils.create(CredentialTable)
        }
    }

    override fun saveUser(user: User, hashedPassword: String) {
        transaction {
            CredentialTable.insert {
                it[userId] = user.id
                it[CredentialTable.hashedPassword] = hashedPassword
            }

            UserTable.insert {
                it[email] = user.email
                it[phoneNumber] = user.phoneNumber
                it[name] = user.name
                it[age] = user.age
            }
        }
    }
}