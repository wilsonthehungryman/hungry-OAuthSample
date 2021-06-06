package com.hungry.oauthsample.infrastructure.sql

import com.hungry.oauthsample.domain.oauth.Credential
import com.hungry.oauthsample.domain.users.User
import com.hungry.oauthsample.domain.users.UserRepository
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

internal object UserTable: UUIDTable() {
    val email: Column<String> = varchar("email", 200).uniqueIndex()
    val phoneNumber: Column<String> = varchar("phone_number", 100)
    val name: Column<String?> = varchar("name", 200).nullable()
    val age: Column<Int?> = integer("age").nullable()
}

// TODO think this needs a refactor, being it's own domain entity
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

    override fun findByEmail(email: String): User? {
        return transaction {
            UserTable.select {
                UserTable.email eq email
            }.map {
                User(
                    it[UserTable.id].toString(),
                    it[UserTable.email],
                    it[UserTable.phoneNumber],
                    it[UserTable.name],
                    it[UserTable.age],
                )
            }.singleOrNull()
        }
    }

    override fun findCredentialById(userId: String): Credential? {
        return transaction {
            CredentialTable.select {
                CredentialTable.userId eq userId
            }.map {
                Credential(
                    it[CredentialTable.userId],
                    it[CredentialTable.hashedPassword],
                )
            }.singleOrNull()
        }
    }
}