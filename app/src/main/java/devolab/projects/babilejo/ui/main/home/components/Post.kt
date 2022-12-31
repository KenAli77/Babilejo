package devolab.projects.babilejo.ui.main.home.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Forum
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.MoreHoriz
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skydoves.landscapist.glide.GlideImage
import devolab.projects.babilejo.R
import devolab.projects.babilejo.domain.model.Post
import devolab.projects.babilejo.domain.model.User
import devolab.projects.babilejo.ui.theme.Yellow
import devolab.projects.babilejo.util.getTimeAgo


@Composable
fun Post(post: Post, locality: String) {
    var userPhotoUrl by remember { mutableStateOf("") }
    var userName by remember { mutableStateOf("") }
    var timeAgo by remember {
        mutableStateOf("")
    }
    var caption by remember { mutableStateOf("") }

    LaunchedEffect(key1 = post.userName) {
        post.userName?.let {
            userName = it
        }
    }

    post.userPhotoUrl?.let {
        userPhotoUrl = it
        Log.e("userPhoto", it)
    }
    post.timeStamp?.let {

        timeAgo = getTimeAgo(it)

    }


    post.caption?.let {
        caption = it
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .background(Color.White),

        ) {

        Column(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.Start,
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                PostHeader(
                    location = locality,
                    userName = userName,
                    time = timeAgo,
                    photoUrl = userPhotoUrl
                )
                Icon(
                    imageVector = Icons.Rounded.MoreHoriz,
                    contentDescription = "more",
                )

            }

            Caption(Modifier.padding(horizontal = 10.dp), text = caption)

            post.photoUrl?.let {
                GlideImage(
                    imageModel = it,
                    contentScale = ContentScale.FillWidth,
                    contentDescription = null,
                    modifier = Modifier.heightIn(max = 500.dp).height(intrinsicSize = IntrinsicSize.Min),

                    )
            }

            PostActionsBar(
                Modifier.padding(horizontal = 10.dp),
                likes = post.likes ?: 0,
                comments = post.comments?.size ?: 0,
                shares = post.shares ?: 0
            )

        }
    }
}


@Preview
@Composable
fun PostPreview() {
    Post()
}


@Composable
fun PostHeader(
    location: String = "no location info", userName: String, time: String, photoUrl: String
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {

        GlideImage(
            contentScale = ContentScale.Crop,
            imageModel = photoUrl,
            contentDescription = null,
            modifier = Modifier
                .clip(
                    CircleShape
                )
                .size(60.dp),

            )
        Column(
            verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.Start
        ) {
            Text(text = userName, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            Text(text = time, color = Color.DarkGray, fontSize = 10.sp)
            Text(
                text = location, color = Yellow, fontSize = 10.sp
            )
        }
    }

}

@Composable
fun PostActionsBar(
    modifier: Modifier = Modifier,
    likes: Int = 0,
    shares: Int = 0,
    comments: Int = 0
) {

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = Icons.Outlined.Favorite,
            contentDescription = null,
            modifier = Modifier.clickable(
                interactionSource = remember { MutableInteractionSource() },
                role = Role.Button,
                enabled = true,
                onClick = {},
                indication = rememberRipple(bounded = false, radius = 16.dp)
            )
        )
        if (likes > 0) {
            Text(
                text = likes.toString(),
                fontSize = 10.sp,
                modifier = Modifier.padding(5.dp),
                fontWeight = FontWeight.SemiBold
            )
        }
        Spacer(modifier = Modifier.width(40.dp))
        Icon(
            imageVector = Icons.Outlined.Forum,
            contentDescription = null,
            modifier = Modifier.clickable(
                interactionSource = remember { MutableInteractionSource() },
                role = Role.Button,
                enabled = true,
                onClick = {},
                indication = rememberRipple(bounded = false, radius = 16.dp)
            )
        )
        if (comments > 0) {
            Text(
                text = comments.toString(),
                fontSize = 10.sp,
                modifier = Modifier.padding(5.dp),
                fontWeight = FontWeight.SemiBold
            )
        }
        Spacer(modifier = Modifier.width(40.dp))
        Icon(
            imageVector = Icons.Outlined.Share,
            contentDescription = null,
            modifier = Modifier.clickable(
                interactionSource = remember { MutableInteractionSource() },
                role = Role.Button,
                enabled = true,
                onClick = {},
                indication = rememberRipple(bounded = false, radius = 16.dp)
            )
        )
        if (shares > 0) {
            Text(
                text = shares.toString(),
                fontSize = 10.sp,
                modifier = Modifier.padding(5.dp),
                fontWeight = FontWeight.SemiBold
            )
        }


        Spacer(modifier = Modifier.width(40.dp))

        Icon(
            imageVector = Icons.Rounded.LocationOn,
            contentDescription = null,
            modifier = Modifier.clickable(
                interactionSource = remember { MutableInteractionSource() },
                role = Role.Button,
                enabled = true,
                onClick = {},
                indication = rememberRipple(bounded = false, radius = 16.dp)
            ),
            tint = Yellow
        )

    }

}

@Composable
fun Caption(modifier: Modifier, text: String = stringResource(id = R.string.lorem_ipsum)) {
    Column(modifier = modifier) {
        Text(
            maxLines = 3, overflow = TextOverflow.Ellipsis, text = text, fontSize = 15.sp
        )

    }

}