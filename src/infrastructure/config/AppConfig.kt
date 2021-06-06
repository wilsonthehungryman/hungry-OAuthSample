package com.hungry.oauthsample.infrastructure.config

import com.auth0.jwt.algorithms.Algorithm
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import infrastructure.config.PemUtils
import org.jetbrains.exposed.sql.Database
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey

class AppConfig {
    val sqlConfig = SQLConfig()
    val keysConfig = RSAKeyConfig()

    class SQLConfig {
        private val config = HikariConfig().apply {
            driverClassName = "org.h2.Driver"
            jdbcUrl = "jdbc:h2:mem:test"
            validate()
        }

        val dataSource = HikariDataSource(config)

        fun connect() {
            Database.connect(dataSource)
        }
    }

    class RSAKeyConfig {
        // TODO, really not liking this, and will make supporting multiple keys (ie rotation) hard, should use key provider
        val publicKey: RSAPublicKey = PemUtils.readPublicKeyFromResourcesFile("/keys/signingpublickey.crt", "RSA") as RSAPublicKey
        val privateKey: RSAPrivateKey = PemUtils.readPrivateKeyFromResourcesFile("/keys/signingkeypkcs8.key", "RSA") as RSAPrivateKey
        val algorithm: Algorithm = Algorithm.RSA256(publicKey, privateKey)
    }
}