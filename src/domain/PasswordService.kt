package com.hungry.oauthsample.domain

import org.mindrot.jbcrypt.BCrypt

class PasswordService {
    // TODO shouldn't be hardcoded, and should be secret and unique per env
    private val pepper = "36f61d32-6c9c-4641-9f32-9a3fce8f1b8f"

    fun hashPassword(password: String): String {
        val passwordWithPepper = BCrypt.hashpw(password, pepper)
        return BCrypt.hashpw(passwordWithPepper, BCrypt.gensalt())
    }

    fun isMatchingPassword(password: String, hashedPassword: String): Boolean {
        val passwordWithPepper = BCrypt.hashpw(password, pepper)
        return BCrypt.checkpw(passwordWithPepper, hashedPassword)
    }

    fun isPreviouslyUsed(password: String, hashedPasswords: Set<String>): Boolean {
        val passwordWithPepper = BCrypt.hashpw(password, pepper)
        return hashedPasswords.stream().anyMatch { BCrypt.checkpw(passwordWithPepper, it) }
    }
}