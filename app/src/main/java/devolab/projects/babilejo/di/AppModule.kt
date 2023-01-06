package devolab.projects.babilejo.di

import android.app.Application
import android.content.Context
import android.location.Geocoder
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
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
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import devolab.projects.babilejo.BuildConfig
import devolab.projects.babilejo.R
import devolab.projects.babilejo.data.location.DefaultLocationTracker
import devolab.projects.babilejo.data.repository.UserAuthRepositoryImpl
import devolab.projects.babilejo.data.repository.MainRepositoryImpl
import devolab.projects.babilejo.domain.repository.UserAuthRepository
import devolab.projects.babilejo.domain.repository.MainRepository
import devolab.projects.babilejo.util.SIGN_IN_REQUEST
import devolab.projects.babilejo.util.SIGN_UP_REQUEST
import java.util.*
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun provideFirebaseAuth() = Firebase.auth

    @Provides
    @Singleton
    fun provideFirebaseFirestore() = Firebase.firestore

    @Provides
    @Singleton
    fun provideFirebaseStorage() = Firebase.storage

    @Provides
    @Singleton
    fun provideOneTapClient(
        @ApplicationContext
        context: Context
    ): SignInClient = Identity.getSignInClient(context)

    @Provides
    @Named(SIGN_IN_REQUEST)
    @Singleton
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
    @Singleton
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
    @Singleton
    fun provideGoogleSignInOptions(
        app: Application
    ) = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(app.getString(R.string.webclient_id))
        .requestEmail()
        .build()

    @Provides
    @Singleton
    fun provideGoogleSignInClient(
        app: Application,
        options: GoogleSignInOptions
    ) = GoogleSignIn.getClient(app, options)


    @Provides
    @ViewModelScoped
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
    @ViewModelScoped
    fun provideMainRepository(
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
    fun providePlacesClient(app: Application): PlacesClient {
        Places.initialize(app.applicationContext, BuildConfig.GOOGLE_MAPS_API_KEY)
        return Places.createClient(app.applicationContext)

    }

    @Provides
    @Singleton
    fun providesLocationTracker(
        app: Application,
        locationProviderClient: FusedLocationProviderClient,
        placesClient: PlacesClient,
        geocoder: Geocoder
    ): DefaultLocationTracker =
        DefaultLocationTracker(
            application = app,
            fusedLocationProviderClient = locationProviderClient,
            placesClient = placesClient,
            geocoder = geocoder
        )

    @Provides
    @Singleton
    fun providesGeoCoder(
        app: Application
    ): Geocoder = Geocoder(app, Locale.getDefault())


}