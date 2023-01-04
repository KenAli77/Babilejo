package devolab.projects.babilejo.ui.main.newPost.state

data class NewPostState(
    val success: Boolean = false,
    val loading: Boolean = true,
    val error:String? = null,
)
