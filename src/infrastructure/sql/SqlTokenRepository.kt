package com.hungry.oauthsample.infrastructure.sql

import com.hungry.oauthsample.domain.tokens.Token
import com.hungry.oauthsample.domain.tokens.TokenRepository
import com.hungry.oauthsample.domain.tokens.TokenService.Companion.ISSUER
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant
import java.util.UUID

internal object TokenTable: UUIDTable("tokens", "token_id") {
    val audience: Column<String> = varchar("audience", 200)
    val subject: Column<String> = varchar("subject", 200)
    val issuedAt: Column<Long> = long("issued_at")
    val expiresAt: Column<Long> = long("expires_at")
}

class SqlTokenRepository: TokenRepository {
    init {
        transaction {
            SchemaUtils.create(TokenTable)
        }
    }

    override fun save(token: Token) {
        TokenTable.insert {
            it[id] = UUID.fromString(token.id)
            it[audience] = token.audience
            it[subject] = token.subject
            it[issuedAt] = token.issuedAt.toEpochMilli()
            it[expiresAt] = token.expiresAt.toEpochMilli()
        }
    }

    override fun findById(id: String): Token? {
        return TokenTable.select {
            TokenTable.id eq UUID.fromString(id)
        }.map {
            deserialize(it)
        }.singleOrNull()
    }

    override fun findByUserId(userId: String): Set<Token> {
        return TokenTable.select {
            TokenTable.subject eq userId
        }.map {
            deserialize(it)
        }.toSet()
    }

    override fun deleteById(id: String) {
        TokenTable.deleteWhere {
            TokenTable.id eq UUID.fromString(id)
        }
    }

    override fun deleteByUserId(userId: String) {
        TokenTable.deleteWhere {
            TokenTable.subject eq userId
        }
    }

    override fun deleteExpiredTokens(now: Instant) {
        TokenTable.deleteWhere {
            TokenTable.expiresAt less now.toEpochMilli()
        }
    }

    private fun deserialize(row: ResultRow): Token {
        return Token(
            ISSUER,
            row[TokenTable.audience],
            row[TokenTable.subject],
            Instant.ofEpochMilli(row[TokenTable.issuedAt]),
            Instant.ofEpochMilli(row[TokenTable.expiresAt]),
            row[TokenTable.id].toString()
        )
    }
}