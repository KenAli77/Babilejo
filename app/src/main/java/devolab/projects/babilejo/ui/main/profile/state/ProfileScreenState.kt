package devolab.projects.babilejo.ui.main.profile.state

import devolab.projects.babilejo.domain.model.Post
import devolab.projects.babilejo.domain.model.UserActivity

data class ProfileScreenState(
    val posts:List<Post>? = null,
    val loading:Boolean = false,
    val activities:List<UserActivity>? = null,
    val error:String? = null


    )
