package devolab.projects.babilejo.ui.main.home.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import devolab.projects.babilejo.ui.authentication.components.Logo
import devolab.projects.babilejo.ui.theme.Yellow
import devolab.projects.babilejo.util.TOP_BAR_HEIGHT
import devolab.projects.babilejo.util.TOP_BAR_ICON_SIZE
import devolab.projects.babilejo.util.isScrolled

@Composable
fun HomeTopBar(
    modifier: Modifier = Modifier,
    lazyListState: LazyListState,
    onChatClick: () -> Unit = {},
    onPostClick: () -> Unit = {},
    openFilters:()->Unit = {}
) {

    TopAppBar(
        modifier = modifier
            .fillMaxWidth()
            // .animateContentSize(animationSpec = tween(300))
            .height(height = TOP_BAR_HEIGHT),
        backgroundColor = Yellow,

        ) {

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Logo(fontSize = 35.sp)

            Row(
                horizontalArrangement = Arrangement.spacedBy(0.dp),
                modifier = Modifier.padding(top = 10.dp, end = 10.dp)
            ) {
                IconButton(onClick = { onPostClick() }) {

                    Icon(
                        imageVector = Icons.Outlined.AddBox,
                        contentDescription = null,
                        modifier = Modifier
                            .size(TOP_BAR_ICON_SIZE),
                        tint = Color.White
                    )
                }

                IconButton(onClick = { onChatClick() }) {
                    Icon(
                        imageVector = Icons.Outlined.Chat,
                        contentDescription = null,
                        modifier = Modifier
                            .size(TOP_BAR_ICON_SIZE),
                        tint = Color.White
                    )
                }

                IconButton(onClick = { openFilters() }) {
                    Icon(
                        imageVector = Icons.Outlined.Tune,
                        contentDescription = null,
                        modifier = Modifier
                            .size(TOP_BAR_ICON_SIZE),
                        tint = Color.White
                    )
                }

            }
        }

    }

}