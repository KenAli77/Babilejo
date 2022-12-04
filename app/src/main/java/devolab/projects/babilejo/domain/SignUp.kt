package devolab.projects.babilejo.domain

import com.google.firebase.auth.AuthResult
import devolab.projects.babilejo.data.repository.interfaces.UserDataRepository
import devolab.projects.babilejo.util.Resource
import javax.inject.Inject

class SignUp @Inject constructor(private val userDataRepository: UserDataRepository) {

    suspend operator fun invoke(
        email: String,
        password: String,
        confirmPassword: String,
        userName: String
    ): Resource<AuthResult> {
        return if (password == confirmPassword) {
            userDataRepository.createNewUser(
                userName = userName,
                userEmailAddress = email,
                userLoginPassword = password,
                confirmPassword = confirmPassword
            )
        } else {
            Resource.Error(message = "the password you enter don't match")
        }
    }
}