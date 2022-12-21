package com.store.wings.di

import com.store.wings.repository.*
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun provideUserRepo(repo: UserRepoImpl): UserRepository

    @Binds
    abstract fun provideProductRepo(repo: ProductRepoImpl): ProductRepository

    @Binds
    abstract fun provideTransactionRepo(repo: TransactionRepoImpl): TransactionRepository
}