package com.hungry.oauthsample.infrastructure.sql

import com.hungry.oauthsample.domain.oauth.Code
import com.hungry.oauthsample.domain.oauth.CodeRepository
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant

internal object CodeTable: UUIDTable("codes") {
    val code: Column<String> = varchar("code", 500).uniqueIndex()
    val clientId: Column<String> = varchar("client_id", 250)
    val userId: Column<String> = varchar("user_id", 250)
    val expiresAt: Column<Long> = long("expires_at")
}

class SqlCodeRepository: CodeRepository {
    init {
        transaction {
            SchemaUtils.create(CodeTable)
        }
    }

    override fun save(code: Code) {
        transaction {
            CodeTable.insert {
                it[CodeTable.code] = code.code
                it[clientId] = code.clientId
                it[userId] = code.userId
                it[expiresAt] = code.expiresAt.toEpochMilli()
            }
        }
    }

    override fun findByCode(code: String): Code? {
        return transaction {
            CodeTable.select {
                CodeTable.code eq code
            }.map {
                Code(
                    it[CodeTable.code],
                    it[CodeTable.clientId],
                    it[CodeTable.userId],
                    Instant.ofEpochMilli(it[CodeTable.expiresAt]),
                )
            }.singleOrNull()
        }
    }

    override fun deleteCode(code: String) {
        transaction {
            CodeTable.deleteWhere {
                CodeTable.code eq code
            }
        }
    }

    override fun deleteExpiredCodes(now: Instant) {
        transaction {
            CodeTable.deleteWhere {
                CodeTable.expiresAt lessEq now.toEpochMilli()
            }
        }
    }
}