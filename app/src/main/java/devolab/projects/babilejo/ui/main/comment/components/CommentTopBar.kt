package devolab.projects.babilejo.ui.main.comment.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import devolab.projects.babilejo.ui.theme.Yellow

@Composable
fun CommentTopBar(modifier: Modifier = Modifier, onNavBack: () -> Unit = {}) {

    Surface(modifier = modifier.fillMaxWidth(), color = Yellow) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier.padding(horizontal = 5.dp, vertical = 2.dp)
        ) {

            Icon(
                imageVector = Icons.Rounded.ArrowBack,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    role = Role.Button,
                    enabled = true,
                    onClick = { onNavBack() },
                    indication = rememberRipple(bounded = false, radius = 16.dp)
                )
            )

            Text(
                text = "Comments",
                color = Color.White,
                fontSize = 22.sp,
                modifier = Modifier.padding(bottom = 5.dp),
                fontWeight = FontWeight.Bold
            )

        }
    }


}

@Preview
@Composable
fun CommentBarPreview() {

    CommentTopBar()

}