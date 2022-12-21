package com.store.wings.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.store.wings.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import java.util.*
import javax.inject.Inject

class UserRepoImpl @Inject constructor(private val database: DatabaseReference): UserRepository{

    override suspend fun login(username: String, password: String): Flow<String?> =
        callbackFlow<String?> {
            val listener = object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val userSnapshot = snapshot.child(snapshot.children.first().key ?: "")
                        if (userSnapshot.child("password").value == password) {
                            trySend(username)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {}

            }
            database.child("users")
                .orderByChild("username")
                .equalTo(username)
                .addValueEventListener(listener)

            awaitClose {
                database.removeEventListener(listener)
            }
        }.flowOn(Dispatchers.IO)

    override suspend fun register(username: String, password: String): Flow<String?> =
        callbackFlow<String?> {
            val listener = object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists().not()){
                        database.child("users").child(Date().time.toString()).setValue(User(username, password))
                            .addOnSuccessListener {
                                trySend(username)
                            }
                    }
                }

                override fun onCancelled(error: DatabaseError) {}

            }
            database.child("users")
                .orderByChild("username")
                .equalTo(username)
                .addValueEventListener(listener)

            awaitClose {
                database.removeEventListener(listener)
            }
        }.flowOn(Dispatchers.IO)
}