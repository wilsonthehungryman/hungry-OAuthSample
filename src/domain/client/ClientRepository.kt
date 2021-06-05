package com.hungry.oauthsample.domain.client

interface ClientRepository {
    fun save(client: Client)
    fun findById(id: String): Client?
}