package com.hungry.oauthsample.domain.oauth

import org.mindrot.jbcrypt.BCrypt

class PasswordService {
    // TODO shouldn't be hardcoded, and should be secret and unique per env
    // just a result from gensalt(), is a secret salt after all
    companion object {
        private const val pepper = "\$2a\$10\$faYzNGod4.Hk/iXFtFJufu"
    }

    fun hashPassword(password: String): String {
        return BCrypt.hashpw(addPepper(password), BCrypt.gensalt())
    }

    fun isMatchingPassword(password: String, hashedPassword: String): Boolean {
        return BCrypt.checkpw(addPepper(password), hashedPassword)
    }

    fun isPreviouslyUsed(password: String, hashedPasswords: Set<String>): Boolean {
        val passwordWithPepper = addPepper(password)
        return hashedPasswords.stream().anyMatch { BCrypt.checkpw(passwordWithPepper, it) }
    }

    private fun addPepper(password: String): String {
        return BCrypt.hashpw(password, pepper)
    }
}