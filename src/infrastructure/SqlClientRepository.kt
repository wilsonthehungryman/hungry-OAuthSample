package com.hungry.oauthsample.infrastructure

import com.hungry.oauthsample.domain.client.Client
import com.hungry.oauthsample.domain.client.ClientRepository
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import java.util.UUID

internal object ClientTable: UUIDTable("clients", "client_id") {
    val secretKey: Column<String> = varchar("secret_key", 250).uniqueIndex()
}

internal object RedirectsTable: Table() {
    val clientId: Column<String> = varchar("client_id", 250)
    val redirectUri: Column<String> = varchar("redirect_uri", 250)
    override val primaryKey = PrimaryKey(clientId, redirectUri)
}

class SqlClientRepository: ClientRepository {
    init {
        SchemaUtils.create(ClientTable)
        SchemaUtils.create(RedirectsTable)
    }

    override fun save(client: Client) {
        ClientTable.insert {
            it[id] = UUID.fromString(client.id)
            it[secretKey] = client.secretKey
        }

        saveRedirects(client)
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

    override fun updateRedirects(client: Client) {
        RedirectsTable.deleteWhere {
            RedirectsTable.clientId eq client.id
        }

        saveRedirects(client)
    }

    private fun saveRedirects(client: Client) {
        client.redirectUris.forEach { uri ->
            RedirectsTable.insert {
                it[clientId] = client.id
                it[redirectUri] = uri
            }
        }
    }
}