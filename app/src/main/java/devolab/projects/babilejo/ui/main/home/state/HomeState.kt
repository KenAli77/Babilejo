package devolab.projects.babilejo.ui.main.home.state

import devolab.projects.babilejo.domain.model.Post

data class HomeState(
    val loading:Boolean = false,
    val data:List<Post>? = null,
    val error:String? = null,
)