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
    val expiresAt: Column<Long> = long("expires_at")
}

class SqlCodeRepository: CodeRepository {
    init {
        transaction {
            SchemaUtils.create(CodeTable)
        }
    }

    override fun save(code: Code) {
        CodeTable.insert {
            it[CodeTable.code] = code.code
            it[clientId] = code.clientId
            it[expiresAt] = code.expiresAt.toEpochMilli()
        }
    }

    override fun findByCode(code: String): Code? {
        return CodeTable.select {
            CodeTable.code eq code
        }.map {
            Code(
                it[CodeTable.code],
                it[CodeTable.clientId],
                Instant.ofEpochMilli(it[CodeTable.expiresAt]),
            )
        }.singleOrNull()
    }

    override fun deleteCode(code: String) {
        CodeTable.deleteWhere {
            CodeTable.code eq code
        }
    }

    override fun deleteExpiredCodes(now: Instant) {
        CodeTable.deleteWhere {
            CodeTable.expiresAt lessEq now.toEpochMilli()
        }
    }
}