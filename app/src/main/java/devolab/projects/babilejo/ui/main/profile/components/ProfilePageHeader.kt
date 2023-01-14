package devolab.projects.babilejo.ui.main.profile.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddBox
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.rounded.PinDrop
import androidx.compose.material.icons.rounded.PushPin
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil.CoilImage
import devolab.projects.babilejo.domain.model.OnlineStatus
import devolab.projects.babilejo.domain.model.User
import devolab.projects.babilejo.ui.authentication.components.Logo
import devolab.projects.babilejo.ui.main.home.components.ProfileImageCircle
import devolab.projects.babilejo.ui.theme.Green
import devolab.projects.babilejo.ui.theme.Yellow
import devolab.projects.babilejo.util.TOP_BAR_HEIGHT
import devolab.projects.babilejo.util.TOP_BAR_ICON_SIZE
import devolab.projects.babilejo.util.getTimeAgo

@Composable
fun ProfilePageHeader(modifier: Modifier = Modifier, user: User, onlineStatus: OnlineStatus) {


    ConstraintLayout(modifier = modifier.fillMaxWidth()) {

        val (profileImage, name, location, bio, online) = createRefs()
        ProfileImageCircle(
            imageUrl = user.photoUrl,
            modifier = Modifier
                .constrainAs(profileImage) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
                .clip(CircleShape)
                .size(60.dp),
        )

        Text(text = user.userName!!, modifier = Modifier.constrainAs(name) {
            top.linkTo(parent.top)
            start.linkTo(profileImage.end, 5.dp)

        }, fontSize = 20.sp, fontWeight = FontWeight.Bold)

        UserLocationBanner(user = user, modifier = Modifier.constrainAs(location) {
            top.linkTo(name.bottom)
            start.linkTo(profileImage.end, 5.dp)
        })



        onlineStatus.online.let {
            it?.let {
                if (it) {
                    Text(
                        text = "ONLINE",
                        color = Green,
                        modifier = Modifier.constrainAs(online) {
                            top.linkTo(parent.top)
                            end.linkTo(parent.end)
                        },
                        fontWeight = FontWeight.Bold
                    )

                } else {
                    onlineStatus.lastOnline?.let {
                        Text(
                            text = getTimeAgo(it),
                            color = Color.DarkGray,
                            modifier = Modifier.constrainAs(online) {
                                top.linkTo(parent.top)
                                end.linkTo(parent.end)
                            })
                    }
                }
            }


        }

        Text(text = "This is a bio", modifier = Modifier.constrainAs(bio) {
            top.linkTo(profileImage.bottom, 5.dp)
            start.linkTo(parent.start, 5.dp)
        })


    }


}

@Composable
fun UserLocationBanner(modifier: Modifier = Modifier, user: User, location: String = "Rome,Italy") {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = Icons.Rounded.PushPin,
            contentDescription = null,
            tint = Color.Red,
            modifier = Modifier.size(15.dp)
        )


        Text(text = location)
    }
}

@Composable
fun ProfileTopBar(modifier: Modifier = Modifier, onNewPost: () -> Unit = {},onEdit:()->Unit = {}) {

    TopAppBar(modifier = modifier.height(height = TOP_BAR_HEIGHT), backgroundColor = Yellow,) {


        Logo(fontSize = 35.sp)

        Row(horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {

            IconButton(onClick = { onEdit() }) {

                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = null,
                    modifier = Modifier
                        .size(TOP_BAR_ICON_SIZE),
                    tint = Color.White
                )
            }
            IconButton(onClick = { onNewPost() }) {

                Icon(
                    imageVector = Icons.Outlined.AddBox,
                    contentDescription = null,
                    modifier = Modifier
                        .size(TOP_BAR_ICON_SIZE),
                    tint = Color.White
                )
            }


        }

    }

}


@Composable
fun UserProfileTopBar(modifier: Modifier = Modifier, onNewPost: () -> Unit = {},onEdit:()->Unit = {}) {

    TopAppBar(modifier = modifier.height(height = TOP_BAR_HEIGHT), backgroundColor = Yellow,) {


        Logo(fontSize = 35.sp)

        Row(horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {

            IconButton(onClick = { onEdit() }) {

                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = null,
                    modifier = Modifier
                        .size(TOP_BAR_ICON_SIZE),
                    tint = Color.White
                )
            }
            IconButton(onClick = { onNewPost() }) {

                Icon(
                    imageVector = Icons.Outlined.AddBox,
                    contentDescription = null,
                    modifier = Modifier
                        .size(TOP_BAR_ICON_SIZE),
                    tint = Color.White
                )
            }


        }

    }


}