package devolab.projects.babilejo.domain.repository

import devolab.projects.babilejo.domain.model.User
import devolab.projects.babilejo.util.RevokeAccessResponse
import devolab.projects.babilejo.util.SignOutResponse
import devolab.projects.babilejo.util.UserDataResponse

interface UserProfileRepository {
    val displayName: String
    val photoUrl: String

    suspend fun getUserData(): UserDataResponse

    suspend fun signOut(): SignOutResponse

    suspend fun revokeAccess(): RevokeAccessResponse
}