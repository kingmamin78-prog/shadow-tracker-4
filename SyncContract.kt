package com.shadowtracker

data class CipherRecord(val id: String, val nonceB64: String, val ciphertextB64: String, val version: Long)
interface PrivateSyncGateway {
 suspend fun upload(record: CipherRecord)
 suspend fun downloadAfter(version: Long): List<CipherRecord>
}
// Network sync and device pairing are intentionally not implemented in this starter.
