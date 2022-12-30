package devolab.projects.babilejo.domain.repository

import android.net.Uri
import com.google.firebase.firestore.QuerySnapshot
import devolab.projects.babilejo.domain.model.Post
import devolab.projects.babilejo.domain.model.Resource
import devolab.projects.babilejo.util.RevokeAccessResponse
import devolab.projects.babilejo.util.SignOutResponse
import devolab.projects.babilejo.util.UserDataResponse

interface MainRepository {
    val displayName: String
    val photoUrl: String
    val currentUserId:String?

    suspend fun getUserData(uid:String?=currentUserId): UserDataResponse

    suspend fun signOut(): SignOutResponse

    suspend fun revokeAccess(): RevokeAccessResponse

    suspend fun addPost(post: Post,imageUri: Uri?=null)

    suspend fun getPosts(): Resource<QuerySnapshot>
}