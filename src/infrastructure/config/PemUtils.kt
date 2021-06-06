package infrastructure.config

import org.bouncycastle.util.io.pem.PemReader
import java.io.File
import java.io.FileNotFoundException
import java.io.FileReader
import java.io.IOException
import java.security.KeyFactory
import java.security.NoSuchAlgorithmException
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.EncodedKeySpec
import java.security.spec.InvalidKeySpecException
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec

// adapted from
// https://gist.github.com/lbalmaceda/9a0c7890c2965826c04119dcfb1a5469
// https://github.com/auth0/java-jwt/blob/master/lib/src/test/java/com/auth0/jwt/PemUtils.java
object PemUtils {
    private fun parsePEMFile(path: String): ByteArray {
        // this::class.java.getResourceAsStream(path)?.bufferedReader()
        val reader = PemReader(this::class.java.getResourceAsStream(path)?.bufferedReader())
        val pemObject = reader.readPemObject()
        val content = pemObject.content
        reader.close()
        return content
    }

    private fun getPublicKey(keyBytes: ByteArray, algorithm: String): PublicKey? {
        var publicKey: PublicKey? = null
        try {
            val kf = KeyFactory.getInstance(algorithm)
            val keySpec: EncodedKeySpec = X509EncodedKeySpec(keyBytes)
            publicKey = kf.generatePublic(keySpec)
        } catch (e: NoSuchAlgorithmException) {
            println("Could not reconstruct the public key, the given algorithm could not be found.")
        } catch (e: InvalidKeySpecException) {
            println("Could not reconstruct the public key")
        }
        return publicKey
    }

    private fun getPrivateKey(keyBytes: ByteArray, algorithm: String): PrivateKey? {
        var privateKey: PrivateKey? = null
        try {
            val kf = KeyFactory.getInstance(algorithm)
            val keySpec: EncodedKeySpec = PKCS8EncodedKeySpec(keyBytes)
            privateKey = kf.generatePrivate(keySpec)
        } catch (e: NoSuchAlgorithmException) {
            println("Could not reconstruct the private key, the given algorithm could not be found.")
        } catch (e: InvalidKeySpecException) {
            println("Could not reconstruct the private key")
        }
        return privateKey
    }

    fun readPublicKeyFromResourcesFile(path: String, algorithm: String): PublicKey? {
        return getPublicKey(parsePEMFile(path), algorithm)
    }

    fun readPrivateKeyFromResourcesFile(path: String, algorithm: String): PrivateKey? {
        return getPrivateKey(parsePEMFile(path), algorithm)
    }
}