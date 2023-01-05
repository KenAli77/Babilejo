package devolab.projects.babilejo.domain.repository

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.QuerySnapshot
import devolab.projects.babilejo.domain.model.*
import devolab.projects.babilejo.util.RevokeAccessResponse
import devolab.projects.babilejo.util.SignOutResponse
import devolab.projects.babilejo.util.UserDataResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface MainRepository {
    val displayName: String
    val photoUrl: String
    val currentUser: FirebaseUser?

    suspend fun getUserData(uid:String?=null): UserDataResponse

    suspend fun signOut(): SignOutResponse

    suspend fun revokeAccess(): RevokeAccessResponse

    suspend fun addPost(post: Post, imageBitmap: Bitmap?=null):Resource<Void>

    suspend fun getPosts(): MutableLiveData<Resource<QuerySnapshot>>

    suspend fun updateUserLocation(location: Location):Resource<Void>

    fun getUserUpdates(): Flow<Resource<List<User>>>

    suspend fun getUserOnlineStatus(uid:String?):Resource<OnlineStatus>
}