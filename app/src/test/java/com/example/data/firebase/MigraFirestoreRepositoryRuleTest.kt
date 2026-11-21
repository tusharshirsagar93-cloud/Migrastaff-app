package com.example.data.firebase

import com.example.base.FirestoreEmulatorTestBase
import com.example.model.CandidateProfile
import com.example.model.CryptoPaymentReceipt
import com.example.model.WorkerProfile
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test
import java.util.UUID

class MigraFirestoreRepositoryRuleTest : FirestoreEmulatorTestBase() {

    private fun sampleWorker(id: Int = 1) = WorkerProfile(
        id = id,
        nameEn = "Jasur Karimov",
        nameRu = "Жасур Каримов",
        countryCode = "UZ",
        profession = "Welder",
        expYears = 7,
        salaryRub = 95000,
        skills = listOf("MIG welding"),
        languages = listOf("Russian", "Uzbek"),
        verified = true,
        bioEn = "Pipeline welder",
        bioRu = "Сварщик трубопроводов",
        activeViewers = 14,
        cityEn = "Moscow",
        cityRu = "Москва",
        responseSpeedEn = "Replies within 1 day",
        responseSpeedRu = "Отвечает в течение 1 дня"
    )

    @Test
    fun saveAndObserveCandidateProfile_authenticatedOwner_succeeds() = runBlocking {
        val uid = signInTestUser(ALICE_EMAIL)
        val repo = MigraFirestoreRepository(firestore, auth)

        val profile = CandidateProfile(
            userId = uid,
            username = "jasur_welder",
            surname = "Karimov",
            firstName = "Jasur",
            countryCode = "UZ",
            profession = "Welder",
            experienceYears = 7,
            expectedSalary = 95000,
            languages = "Russian, Uzbek",
            skills = "MIG welding, Pipe fitting",
            about = "7 years industrial welding experience.",
            phone = "+79120000001",
            dateOfBirth = "1992-04-12",
            photoCount = 2
        )

        val saveRes = withTimeout(DEFAULT_TIMEOUT_MS) { repo.saveCandidateProfile(profile) }
        assertTrue(saveRes.isSuccess)

        val stream = withTimeout(FLOW_TIMEOUT_MS) {
            repo.observeProfile(uid).first { it is StreamResult.Success && it.data != null }
        } as StreamResult.Success
        assertEquals("Karimov", stream.data?.surname)

        // Update path works too
        val updatedRes = withTimeout(DEFAULT_TIMEOUT_MS) {
            repo.saveCandidateProfile(profile.copy(expectedSalary = 110000))
        }
        assertTrue(updatedRes.isSuccess)
    }

    @Test
    fun favoritesAndMessagesAndCrypto_authenticatedOwner_succeed() = runBlocking {
        val uid = signInTestUser(ALICE_EMAIL)
        val repo = MigraFirestoreRepository(firestore, auth)
        val dynamicWorkerId = kotlin.random.Random.nextInt(1000, 999999)
        val worker = sampleWorker(id = dynamicWorkerId)

        val toggleRes = withTimeout(DEFAULT_TIMEOUT_MS) { repo.toggleFavorite(worker, currentlySaved = false) }
        assertTrue(toggleRes.isSuccess)

        val widStr = dynamicWorkerId.toString()
        val favStream = withTimeout(FLOW_TIMEOUT_MS) {
            repo.observeFavorites(uid).first {
                it is StreamResult.Success && it.data.any { fav -> fav.workerId == widStr }
            }
        } as StreamResult.Success
        assertEquals(1, favStream.data.count { it.workerId == widStr })

        val msgRes = withTimeout(DEFAULT_TIMEOUT_MS) {
            repo.sendMessage("Hello Director", "Здравствуйте, директор", "en")
        }
        assertTrue(msgRes.isSuccess)

        val refId = "MS-CR-${UUID.randomUUID().toString().take(8)}"
        val payRes = withTimeout(DEFAULT_TIMEOUT_MS) {
            repo.submitCryptoReceipt(
                CryptoPaymentReceipt(
                    userId = uid,
                    refId = refId,
                    planKey = "pro",
                    coinKey = "USDT_TRC20",
                    amountCrypto = "5.39 USDT",
                    amountRub = 499.0,
                    txHash = "0xtxhash_${UUID.randomUUID().toString().take(8)}",
                    payerContact = "alice@example.com"
                )
            )
        }
        assertTrue(payRes.isSuccess)
    }

    @Test
    fun crossUserRead_deniedWithPermissionDenied() = runBlocking {
        val aliceUid = signInTestUser(ALICE_EMAIL)
        val aliceRepo = MigraFirestoreRepository(firestore, auth)
        withTimeout(DEFAULT_TIMEOUT_MS) {
            aliceRepo.sendMessage("Private msg", "Личное сообщение", "en").getOrThrow()
        }

        signInTestUser(BOB_EMAIL)
        try {
            withTimeout(DEFAULT_TIMEOUT_MS) {
                firestore.collection("users").document(aliceUid)
                    .collection("messages").get().await()
            }
            fail("Expected PERMISSION_DENIED for cross-user messages read")
        } catch (e: Exception) {
            val fsEx = e.unwrapFirestoreException()
            assertNotNull("Expected FirebaseFirestoreException, got $e", fsEx)
            assertEquals(FirebaseFirestoreException.Code.PERMISSION_DENIED, fsEx?.code)
        }
    }

    @Test
    fun unauthenticatedStream_failsWithPermissionDenied() = runBlocking {
        auth.signOut()
        val repo = MigraFirestoreRepository(firestore, auth)
        val firestoreException: FirebaseFirestoreException? = try {
            val result = withTimeout(FLOW_TIMEOUT_MS) {
                repo.observeProfile("alice_uid").first { it !is StreamResult.Loading }
            }
            if (result is StreamResult.Error) {
                result.cause.unwrapFirestoreException()
            } else {
                null
            }
        } catch (e: Exception) {
            e.unwrapFirestoreException()
        }
        assertNotNull("Expected PERMISSION_DENIED exception when signed out", firestoreException)
        assertEquals(FirebaseFirestoreException.Code.PERMISSION_DENIED, firestoreException?.code)
    }

    private companion object {
        const val ALICE_EMAIL = "alice.migrastaff@test.com"
        const val BOB_EMAIL = "bob.migrastaff@test.com"
        const val DEFAULT_TIMEOUT_MS = 5000L
        const val FLOW_TIMEOUT_MS = 3000L
    }
}
