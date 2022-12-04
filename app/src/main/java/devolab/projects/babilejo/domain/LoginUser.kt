package devolab.projects.babilejo.domain

import com.google.firebase.auth.AuthResult
import devolab.projects.babilejo.data.repository.interfaces.UserDataRepository
import devolab.projects.babilejo.util.Resource
import javax.inject.Inject

class LoginUser @Inject constructor(
    private val userDataRepository: UserDataRepository
) {


    suspend operator fun invoke(email: String, password: String): Resource<AuthResult> {
        return userDataRepository.loginUser(email = email, password = password)
    }
}