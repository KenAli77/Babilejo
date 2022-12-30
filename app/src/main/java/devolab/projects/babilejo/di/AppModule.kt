package devolab.projects.babilejo.di

import android.app.Application
import android.content.Context
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import devolab.projects.babilejo.R
import devolab.projects.babilejo.data.location.DefaultLocationTracker
import devolab.projects.babilejo.data.repository.UserAuthRepositoryImpl
import devolab.projects.babilejo.data.repository.MainRepositoryImpl
import devolab.projects.babilejo.domain.repository.UserAuthRepository
import devolab.projects.babilejo.domain.repository.MainRepository
import devolab.projects.babilejo.ui.authentication.UserAuthViewModel
import devolab.projects.babilejo.util.SIGN_IN_REQUEST
import devolab.projects.babilejo.util.SIGN_UP_REQUEST
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    fun provideFirebaseAuth() = Firebase.auth

    @Provides
    fun provideFirebaseFirestore() = Firebase.firestore

    @Provides
    fun provideFirebaseStorage() = Firebase.storage
    @Provides
    fun provideOneTapClient(
        @ApplicationContext
        context: Context
    ): SignInClient = Identity.getSignInClient(context)

    @Provides
    @Named(SIGN_IN_REQUEST)
    fun provideSignInRequest(app: Application) =
        BeginSignInRequest.builder().setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setServerClientId(app.getString(R.string.webclient_id))
                .setFilterByAuthorizedAccounts(true)
                .build()
        )
            .setAutoSelectEnabled(true)
            .build()


    @Provides
    @Named(SIGN_UP_REQUEST)
    fun provideSignUpRequest(app: Application) =
        BeginSignInRequest.builder().setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setServerClientId(app.getString(R.string.webclient_id))
                .setFilterByAuthorizedAccounts(false)
                .build()
        )
            .build()


    @Provides
    fun provideGoogleSignInOptions(
        app: Application
    ) = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(app.getString(R.string.webclient_id))
        .requestEmail()
        .build()

    @Provides
    fun provideGoogleSignInClient(
        app: Application,
        options: GoogleSignInOptions
    ) = GoogleSignIn.getClient(app, options)


    @Provides
    @Singleton
    fun providesAuthViewModel(
        repo: UserAuthRepositoryImpl,
        oneTapClient: SignInClient,
        userProfileRepo: MainRepositoryImpl
    ): UserAuthViewModel {
        return UserAuthViewModel(repo, oneTapClient, userProfileRepo = userProfileRepo)
    }


    @Provides
    @Singleton
    fun provideUserAuthRepository(
        auth: FirebaseAuth,
        oneTapClient: SignInClient,
        @Named(SIGN_IN_REQUEST)
        signInRequest: BeginSignInRequest,
        @Named(SIGN_UP_REQUEST)
        signUpRequest: BeginSignInRequest,
        db: FirebaseFirestore
    ): UserAuthRepository = UserAuthRepositoryImpl(
        auth = auth,
        oneTapClient = oneTapClient,
        signInRequest = signInRequest,
        signUpRequest = signUpRequest,
        db = db
    )

    @Provides
    @Singleton
    fun provideUserProfileRepository(
        auth: FirebaseAuth,
        oneTapClient: SignInClient,
        signInClient: GoogleSignInClient,
        db: FirebaseFirestore,
        firestore: FirebaseFirestore,
        storage: FirebaseStorage
    ): MainRepository = MainRepositoryImpl(
        auth = auth,
        oneTapClient = oneTapClient,
        signInClient = signInClient,
        db = db,
        firestore = firestore,
        storage = storage
    )

    @Provides
    @Singleton
    fun provideFusedLocationProviderClient(app: Application): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(app)
    }


    @Provides
    @Singleton
    fun providesLocationTracker(
        app: Application,
        locationProviderClient: FusedLocationProviderClient
    ): DefaultLocationTracker =
        DefaultLocationTracker(application = app, fusedLocationProviderClient = locationProviderClient)


}