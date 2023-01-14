package devolab.projects.babilejo.ui.main.profile.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import devolab.projects.babilejo.domain.model.Post
import devolab.projects.babilejo.domain.model.User
import devolab.projects.babilejo.ui.main.home.components.Post
import devolab.projects.babilejo.ui.theme.Yellow
import kotlinx.coroutines.launch

data class TabRowItem(
    val title: String,
    val screen: @Composable () -> Unit,
)


@OptIn(ExperimentalPagerApi::class)
@Composable
fun ProfilePager(modifier: Modifier=Modifier,posts: List<Post>,user: User) {

    val tabRowItems = listOf(
        TabRowItem(
            title = "Activity",
            screen = { ActivitiesPage() },
        ),
        TabRowItem(
            title = "Posts",
            screen = { PostsPage(posts,user) },
        ),

        )

    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    TabRow(
        selectedTabIndex = pagerState.currentPage,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier.pagerTabIndicatorOffset(pagerState, tabPositions),
                color = Yellow
            )

        },
        modifier = modifier,
        backgroundColor = Color.Transparent
    ) {
        tabRowItems.forEachIndexed { index, tabRowItem ->

            Tab(
                selected = pagerState.currentPage == index,
                onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } },
                text = {
                    Text(
                        text = tabRowItem.title,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            )


        }
    }
    
    HorizontalPager(count = tabRowItems.size,
    state = pagerState) {
        tabRowItems[pagerState.currentPage].screen()
    }

}


@Composable
fun ActivitiesPage() {
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center,
    ) {

        Text(
            text = "Activity",

            )

    }

}

@Composable
fun PostsPage(posts:List<Post> = ArrayList<Post>(),user: User) {
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center,
    ) {

        LazyColumn(){

            items(posts){ item->

             Post(
                    post = item ,
                    locality = item.place!! ,
                    onProfileClick = {  },
                    liked = item.likes!!.contains(user)
                )

            }

        }

    }

}

