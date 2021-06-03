package com.hungry.oauthsample.domain.tokens

import com.hungry.oauthsample.domain.User
import io.jsonwebtoken.JwtBuilder
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import java.time.Instant
import java.util.Date
import java.util.UUID

class TokenService {
    val issuer = "hungry"
    val signatureAlgorithm = SignatureAlgorithm.HS256
    val signingKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJeYMv2SpAiG0Us7ItHHthQRbPDPXupNrhhNIDkVqgaODSKxDYXLH3bUS8CsrHYxR5iVVAN6UH+cO9wqWMk0j4I9TB7yA3gxXpakzKSvNNj0pGbrvJTZUx4MFW3zNSJvxfgdIpnhxciAXV/eFOaGJ+Ep5NIg7pThQ1mpTIQ9sMhdAgMBAAECgYBY94vFYZA/KNAf9L7Emw5yzJ9A3JL5s0kfHfRxTBOS8T8oQvyKruDyXjlvBGfanaVFGS5LNDeWDNzco0WkH+KwR3nXGvcJNKbHX/iClswaIL+vGv2qnZLmFlXfgNqKHDQR5Z6YmOwiX1NWNlt8epQA09uFv0AkZEtAd2CqPHZPSQJBAMm0/EufMJrQgNZewgutQDa90bqXs9l2GhX5Fs0rOJxEhhTMkuAlI/dP0i0xmlBebTtBuAMFhk2HLvvct+rmxkcCQQDAZh4GgJRYpEoGEtC2XIAEG83y/GZ0qic3WjA05PrcQ33Qy7dFiiaAoORaY7mKMbBDKjuvJK7cr7A+iqMrQzo7AkBExC4rfectMJ5r9zxCjNtSjl5dNfD713bHPPeYFP8kz3vCxYfhYOglHZJzE/EfK0IkLKCWbyC9d7P9697L98drAkEAkoWyXBVT5GXJkfKGTDsJqxdZsrERfcuPbhIbIE051sD3cimSez0IsXDWNxg3Vs0chSoGpg1ztdF+kUKXnPznvQJAAUNgyHNwqZqVUJ/8PAFBecYlK2EtFwTJheTkg/Pbkw3aC7oMbWe5ZaSd5jNRrUXykBSk4G/91oGAn9I0Rb7xkA=="

    fun sign(builder: JwtBuilder) {
        builder.signWith(signatureAlgorithm, signingKey)
    }

    fun createAccessToken(user: User, now: Instant, audience: String): String {
        val builder = AccessToken(
            issuer,
            audience,
            user.id,
        ).toJwtBuilder(now)

        builder.signWith(SignatureAlgorithm.HS256, signingKey)
        sign(builder)

        return builder.compact()
    }

    fun createRefreshToken(accessToken: AccessToken) {

    }
}