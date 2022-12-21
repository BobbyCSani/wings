package com.store.wings.repository

import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun login(username: String, password: String): Flow<String?>
    suspend fun register(username: String, password: String): Flow<String?>
}