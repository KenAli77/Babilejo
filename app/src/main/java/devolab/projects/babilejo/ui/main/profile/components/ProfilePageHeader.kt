package devolab.projects.babilejo.ui.main.profile.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PinDrop
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.skydoves.landscapist.coil.CoilImage
import devolab.projects.babilejo.domain.model.OnlineStatus
import devolab.projects.babilejo.domain.model.User
import devolab.projects.babilejo.util.getTimeAgo

@Composable
fun ProfilePageHeader(modifier: Modifier = Modifier, user: User, onlineStatus: OnlineStatus) {


    val status =
        if (onlineStatus.online!!) "ONLINE" else if (onlineStatus.lastOnline != null) getTimeAgo(
            onlineStatus.lastOnline
        ) else "null"

    ConstraintLayout(modifier = modifier) {

        val (profileImage, name, location, bio, online) = createRefs()
        CoilImage(imageModel = { user.photoUrl }, modifier = modifier.constrainAs(profileImage) {
            top.linkTo(parent.top)
            start.linkTo(parent.start)
        })

        Text(text = user.userName!!, modifier = Modifier.constrainAs(name) {
            top.linkTo(parent.top)
            start.linkTo(profileImage.end, 5.dp)

        })

        UserLocationBanner(user = user, modifier = Modifier.constrainAs(location) {
            top.linkTo(name.bottom)
            start.linkTo(profileImage.end, 5.dp)
        })



        onlineStatus.online.let {
            if (it) {
                Text(text = "ONLINE", color = Color.Green, modifier = Modifier.constrainAs(online){
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                })

            } else {
                onlineStatus.lastOnline?.let {
                    Text(text = getTimeAgo(it), color = Color.DarkGray,modifier = Modifier.constrainAs(online){
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                    })
                }
            }

        }

        Text(text = "This is a bio", modifier = Modifier.constrainAs(bio){
            top.linkTo(profileImage.bottom,5.dp)
            start.linkTo(parent.start)
        })


    }


}

@Composable
fun UserLocationBanner(modifier: Modifier = Modifier, user: User, location: String = "Rome,Italy") {
    Row(modifier = modifier) {

        IconButton(onClick = { /*TODO*/ }) {
            Icon(imageVector = Icons.Rounded.PinDrop, contentDescription = null, tint = Color.Red)
        }

        Text(text = location)
    }
}