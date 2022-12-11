package devolab.projects.babilejo.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Explore
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Message
import androidx.compose.ui.graphics.vector.ImageVector
import devolab.projects.babilejo.R


object Graph {
    const val ROOT_ROUTE = "root_route"
    const val AUTH_ROUTE = "auth_route"
    const val MAIN_ROUTE = "main_route"
}

sealed class AuthScreens(
    val route: String,
    @StringRes
    val title: Int, val icon: ImageVector? = null
) {

    object Login : BottomBarScreens(route = "login_screen", title = R.string.login)
    object Signup : BottomBarScreens(route = "signup_screen", title = R.string.signup)

}

sealed class BottomBarScreens(
    val route: String,
    @StringRes
    val title: Int, val icon: ImageVector? = null
) {

    object Home : BottomBarScreens(route = "home_screen", title = R.string.home, icon = Icons.Rounded.Home)
    object Explore : BottomBarScreens(route = "explore_screen", title = R.string.explore, icon = Icons.Rounded.Explore)
    object Messages : BottomBarScreens(route = "messages_screen", title = R.string.messages, icon = Icons.Rounded.Message)


}
