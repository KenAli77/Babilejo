package devolab.projects.babilejo.di

import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import devolab.projects.babilejo.data.repository.interfaces.UserDataRepository
import devolab.projects.babilejo.domain.SignUp
import devolab.projects.babilejo.domain.LoginUser
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideLoginUser(userDataRepository: UserDataRepository):LoginUser {
        return LoginUser(userDataRepository)
    }

    @Provides
    @Singleton
    fun provideSignupUser(userDataRepository: UserDataRepository):SignUp {
        return SignUp(userDataRepository)
    }


}