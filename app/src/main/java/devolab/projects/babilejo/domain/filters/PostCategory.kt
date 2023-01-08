package devolab.projects.babilejo.domain.filters

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.ui.graphics.vector.ImageVector
import devolab.projects.babilejo.R

enum class PostCategory(
    val id: Int,
    val icon: ImageVector,
    @StringRes
    val title: Int
) {

    People(1, Icons.Rounded.Groups, R.string.people_filter_title),
    Events(2, Icons.Rounded.LocalActivity, R.string.events_filter_title),
    Stores(3, Icons.Rounded.Store, R.string.stores_filter_title),
    Culture(5, Icons.Rounded.TheaterComedy, R.string.culture_filter_title),
    Party(6, Icons.Rounded.Celebration, R.string.party_filter_title),
    OutDoor(7, Icons.Rounded.Hiking, R.string.outdoors_filter_title),
    Sport(8, Icons.Rounded.SportsBasketball, R.string.sports_filter_title)

}