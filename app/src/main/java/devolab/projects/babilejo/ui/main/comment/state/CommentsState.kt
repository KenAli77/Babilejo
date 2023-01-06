package devolab.projects.babilejo.ui.main.comment.state

import devolab.projects.babilejo.domain.model.Comment

data class CommentsState(
    val comments:List<Comment>?=null,
    val loading:Boolean = false,
    val error:String? = null
)
