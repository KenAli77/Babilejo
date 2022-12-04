package devolab.projects.babilejo.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import devolab.projects.babilejo.data.repository.implementation.UserDataRepositoryImpl
import devolab.projects.babilejo.data.repository.interfaces.UserDataRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UserDataRepoModule {

    @Binds
    @Singleton
    abstract fun bindUserDataRepository(userDataRepositoryImpl: UserDataRepositoryImpl): UserDataRepository
}