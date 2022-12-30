package devolab.projects.babilejo.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.ui.graphics.vector.ImageVector
import devolab.projects.babilejo.R


object Graph {
    const val ROOT_ROUTE = "root_route"
    const val AUTH_ROUTE = "auth_route"
    const val MAIN_ROUTE = "main_route"
}

sealed class Screens(
    val route: String,
    @StringRes
    val title: Int, val icon: ImageVector? = null
) {
    //Auth screens
    object Login : Screens(route = "login_screen", title = R.string.login)
    object Signup : Screens(route = "signup_screen", title = R.string.signup)

    //BottomBar screens
    object Home : Screens(route = "home_screen", title = R.string.home, icon = Icons.Rounded.Home)
    object Explore : Screens(route = "explore_screen", title = R.string.explore, icon = Icons.Rounded.Explore)
    object Messages : Screens(route = "messages_screen", title = R.string.messages, icon = Icons.Rounded.Message)
    object Profile : Screens(route = "profile_screen", title = R.string.profile, icon = Icons.Rounded.Person)
    object Settings : Screens(route = "settings_screen", title = R.string.settings, icon = Icons.Rounded.Settings)


    //
    object NewPost:Screens(route = "new_post_screen", title = R.string.new_post)



}
