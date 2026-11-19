package com.shadowtracker

import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

/** AES-GCM helper scaffold. Production keys must be generated and protected by Android Keystore. */
object CryptoVault {
 data class Sealed(val nonce: ByteArray, val ciphertext: ByteArray)
 fun createKey(): SecretKey = KeyGenerator.getInstance("AES").apply { init(256) }.generateKey()
 fun seal(key: SecretKey, clear: ByteArray): Sealed {
  val nonce = ByteArray(12).also { SecureRandom().nextBytes(it) }
  val cipher = Cipher.getInstance("AES/GCM/NoPadding")
  cipher.init(Cipher.ENCRYPT_MODE, key, GCMParameterSpec(128, nonce))
  return Sealed(nonce, cipher.doFinal(clear))
 }
 fun open(key: SecretKey, sealed: Sealed): ByteArray {
  val cipher = Cipher.getInstance("AES/GCM/NoPadding")
  cipher.init(Cipher.DECRYPT_MODE, key, GCMParameterSpec(128, sealed.nonce))
  return cipher.doFinal(sealed.ciphertext)
 }
}
