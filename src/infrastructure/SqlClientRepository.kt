package com.hungry.oauthsample.infrastructure

import com.hungry.oauthsample.domain.client.Client
import com.hungry.oauthsample.domain.client.ClientRepository
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import java.util.UUID

internal object ClientTable: UUIDTable("clients", "client_id") {
    val secretKey: Column<String> = varchar("secret_key", 250).uniqueIndex()
}

class SqlClientRepository: ClientRepository {
    override fun save(client: Client) {
        ClientTable.insert {
            it[id] = UUID.fromString(client.id)
            it[secretKey] = client.secretKey
        }
    }

    override fun findById(id: String): Client? {
        return ClientTable.select {
            ClientTable.id eq UUID.fromString(id)
        }.map {
            Client(
                it[ClientTable.id].toString(),
                it[ClientTable.secretKey],
            )
        }.singleOrNull()
    }
}