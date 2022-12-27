package devolab.projects.babilejo.ui.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import devolab.projects.babilejo.data.repository.UserProfileRepositoryImpl
import devolab.projects.babilejo.domain.model.Resource
import devolab.projects.babilejo.util.UserDataResponse
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val userProfileRepo: UserProfileRepositoryImpl

):ViewModel() {

    var userDataState by mutableStateOf<UserDataResponse>(Resource.Success(null))


    init {
        getUserData()
    }


    fun getUserData()= viewModelScope.launch {
        userDataState = Resource.Loading()
        userDataState = userProfileRepo.getUserData()
    }
}