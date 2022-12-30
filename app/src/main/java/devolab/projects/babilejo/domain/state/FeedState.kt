package devolab.projects.babilejo.domain.state

import devolab.projects.babilejo.domain.model.Post

data class FeedState(
    val loading:Boolean = false,
    val data:List<Post>? = null,
    val error:String? = null,
)