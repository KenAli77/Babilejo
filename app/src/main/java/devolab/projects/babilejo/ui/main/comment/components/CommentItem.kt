package devolab.projects.babilejo.ui.main.comment.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil.CoilImage
import devolab.projects.babilejo.domain.model.Comment
import devolab.projects.babilejo.ui.theme.DarkYellow
import devolab.projects.babilejo.ui.theme.Yellow
import devolab.projects.babilejo.util.getTimeAgo

@Composable
fun CommentItem(modifier: Modifier = Modifier, comment: Comment = Comment()) {

    ConstraintLayout(modifier = modifier.fillMaxWidth().padding(horizontal = 5.dp)) {
        val (profileImage, content, userName, time, reply) = createRefs()

        CoilImage(
            imageModel = { comment.userPhotoUrl },
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .constrainAs(profileImage) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                },
            imageOptions = ImageOptions(
                alignment = Alignment.Center,
                contentScale = ContentScale.Crop,
            ),
        )

        Text(text = comment.userName ?: "user", modifier = Modifier.constrainAs(userName) {
            top.linkTo(profileImage.top)
            start.linkTo(profileImage.end, 10.dp)
        }, fontWeight = FontWeight.Bold)

        Text(
            text = getTimeAgo(comment.timeStamp!!),
            color = Color.DarkGray,
            modifier = Modifier.constrainAs(time) {
                top.linkTo(userName.top)
                start.linkTo(userName.end, 10.dp)
                bottom.linkTo(userName.bottom)
            },
            fontSize = 12.sp
        )

        CommentText(modifier = Modifier.constrainAs(content) {
            top.linkTo(userName.bottom)
            start.linkTo(profileImage.end, 10.dp)
        }, text = comment.text!!)

        Text(
            text = "Reply", modifier = Modifier.constrainAs(reply) {
                top.linkTo(content.bottom, 5.dp)
                start.linkTo(profileImage.end, 10.dp)
            },
            color = DarkYellow,
            fontSize = 12.sp
        )

        //    CommentLikesItem(modifier = Modifier.constrainAs(like))

    }

}


@Composable
fun CommentText(modifier: Modifier = Modifier, text: String) {

    Text(text = text, fontSize = 16.sp, color = Color.Black, modifier = modifier)
}

@Composable
fun CommentLikesItem(modifier: Modifier) {

}

@Preview
@Composable
fun ItemPreview() {
    CommentItem()
}
