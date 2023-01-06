package devolab.projects.babilejo.ui.main.comment.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import devolab.projects.babilejo.domain.model.Comment

@Composable
fun Comments(modifier: Modifier=Modifier, comments: List<Comment>?) {

    comments?.let { comms->
        LazyColumn(modifier = modifier, contentPadding = PaddingValues(vertical = 10.dp)) {

            items(comms) { item ->
                CommentItem(modifier = Modifier.padding(vertical = 10.dp), comment = item)

            }
        }

    }?: Box(modifier=Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text ="be the first to comment!")
    }

}