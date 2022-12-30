package devolab.projects.babilejo.ui.main.home.components

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skydoves.landscapist.glide.GlideImage
import devolab.projects.babilejo.R
import devolab.projects.babilejo.ui.theme.Yellow


@Composable
fun Post() {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .padding(vertical = 5.dp)
            .background(Color.White),

    ) {

        Column(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
                ,
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.Start,
        ) {
            Row(Modifier.fillMaxWidth().padding(horizontal = 10.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                PostHeader()
                Icon(
                    imageVector = Icons.Rounded.MoreHoriz,
                    contentDescription = "more",
                )

            }

            Caption(Modifier.padding(horizontal = 10.dp))

            GlideImage(
                imageModel = "https://images.pexels.com/photos/1745747/pexels-photo-1745747.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
                contentScale = ContentScale.Crop,
                contentDescription = null,
                modifier = Modifier
                    .height(300.dp),

                )

            PostActionsBar(Modifier.padding(horizontal = 10.dp))

        }

    }

}

@Preview
@Composable
fun PostPreview() {
    Post()
}


@Composable
fun PostHeader() {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {

        GlideImage(
            contentScale = ContentScale.Crop,
            imageModel = "https://images.pexels.com/photos/14584497/pexels-photo-14584497.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
            contentDescription = null,
            modifier = Modifier
                .clip(
                    CircleShape
                )
                .size(60.dp),

            )
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(text = "Riley", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            Text(text = "1 hour ago", color = Color.DarkGray, fontSize = 10.sp)
            Text(
                text = "Portofino Market",
                fontStyle = FontStyle.Italic,
                color = Yellow,
                fontSize = 10.sp
            )
        }
    }

}

@Composable
fun PostActionsBar(modifier: Modifier=Modifier) {

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
        Text(
            text = "200",
            fontSize = 10.sp,
            modifier = Modifier.padding(5.dp),
            fontWeight = FontWeight.SemiBold
        )
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
        Text(
            text = "16",
            fontSize = 10.sp,
            modifier = Modifier.padding(5.dp),
            fontWeight = FontWeight.SemiBold
        )
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
        Text(
            text = "7",
            fontSize = 10.sp,
            modifier = Modifier.padding(5.dp),
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.width(40.dp))

        Icon(
            imageVector = Icons.Rounded.LocationOn,
            contentDescription = null,
            modifier = Modifier.clickable(
                interactionSource = remember { MutableInteractionSource() },
                role = Role.Button,
                enabled = true,
                onClick = {},
                indication = rememberRipple(bounded = false, radius = 16.dp)),
            tint = Yellow
        )

    }

}

@Composable
fun Caption(modifier: Modifier,text:String=stringResource(id = R.string.lorem_ipsum)) {
    Column(modifier=modifier) {
        Text(
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            text = text,
            fontSize = 15.sp
        )

    }

}