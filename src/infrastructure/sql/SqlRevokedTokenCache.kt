package com.hungry.oauthsample.infrastructure.sql

import com.hungry.oauthsample.domain.tokens.RevokedTokenCache
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant
import java.util.UUID

// TODO as the name implies this really really should be a cache
internal object TokenCacheTable: UUIDTable("token_cache", "token_id") {
    val expiresAt: Column<Long> = long("expires_at")
}

class SqlRevokedTokenCache: RevokedTokenCache {
    init {
        transaction {
            SchemaUtils.create(TokenCacheTable)
        }
    }

    override fun addRevokedToken(id: String, expiration: Instant) {
        transaction {
            TokenCacheTable.insert {
                it[this.id] = UUID.fromString(id)
                it[expiresAt] = expiration.toEpochMilli()
            }
        }
    }

    override fun isRevokedToken(id: String): Boolean {
        return transaction {
            TokenCacheTable.select {
                TokenCacheTable.id eq UUID.fromString(id)
            }.any()
        }
    }

    override fun removeExpiredTokens(now: Instant) {
        transaction {
            TokenCacheTable.deleteWhere {
                TokenCacheTable.expiresAt less now.toEpochMilli()
            }
        }
    }
}