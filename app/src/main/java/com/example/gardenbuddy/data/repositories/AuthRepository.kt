package com.example.gardenbuddy.data.repositories

import com.example.gardenbuddy.data.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.Date

object AuthRepository {


    private val auth = FirebaseAuth.getInstance()
    private val usersCollection = FirebaseFirestore.getInstance().collection("users")

    // Sign Up
    suspend fun signUp(email: String, password: String, name: String, weight: Double, birthdate: Date): Result<User> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val userId = result.user?.uid ?: ""

            // Create new object user
            val newUser = User(
                userId = userId,
                name = name,
                email = email,
                weight = weight,
                birthdate = birthdate
            )

            // Save user in Firestore
            usersCollection.document(userId).set(newUser).await()

            Result.success(newUser)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Sign In
    suspend fun logIn(email: String, password: String): Result<User> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val userId = result.user?.uid ?: ""
            val userDoc = usersCollection.document(userId).get().await()

            if (userDoc.exists()) {
                val user = userDoc.toObject(User::class.java)
                if (user != null) {
                    Result.success(user)
                } else {
                    Result.failure(Exception("User data not found"))
                }
            } else {
                Result.failure(Exception("User document does not exist"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Sign Out
    fun signOut() {
        auth.signOut()
    }

    // get logged user (if logged)
    suspend fun getUserProfile(userId: String): Result<User> {
        return try {
            val userDoc = usersCollection.document(userId).get().await()
            if (userDoc.exists()) {
                val user = userDoc.toObject(User::class.java)
                if (user != null) {
                    Result.success(user)
                } else {
                    Result.failure(Exception("User data not found"))
                }
            } else {
                Result.failure(Exception("User document does not exist"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // update user profile
    suspend fun updateUserProfile(user: User): Result<Unit> {
        return try {
            usersCollection.document(user.userId).set(user).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Helpers
    fun getCurrentUserId(): String? = auth.currentUser?.uid
}
