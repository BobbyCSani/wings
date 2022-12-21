package com.store.wings.di

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseReferenceModule {

    @Provides
    @Singleton
    fun bindDBRef(): DatabaseReference = FirebaseDatabase.getInstance().reference

}