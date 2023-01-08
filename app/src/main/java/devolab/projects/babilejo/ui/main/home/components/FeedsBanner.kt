package devolab.projects.babilejo.ui.main.home.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.google.accompanist.flowlayout.FlowRow
import devolab.projects.babilejo.domain.filters.PostCategory
import devolab.projects.babilejo.ui.theme.DarkYellow
import devolab.projects.babilejo.ui.theme.Yellow

@Composable
fun FeedBanner(
    modifier: Modifier = Modifier,
    filters: (Set<PostCategory>) -> Unit = {},
    filterList: MutableSet<PostCategory>
) {

    Surface(modifier = modifier, color = Yellow) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {

            FilterChips(
                onFilter = {
                    filters(it)
                },
                filters = filterList
            )


        }
    }

}


@Composable
fun FilterChips(
    onFilter: (Set<PostCategory>) -> Unit,
    modifier: Modifier = Modifier,
    filters: MutableSet<PostCategory>
) {

    FlowRow(modifier = modifier
        .fillMaxWidth()
        .padding(5.dp), crossAxisSpacing = 2.dp, mainAxisSpacing = 2.dp) {


        val selectedFilters = remember { filters }

        PostCategory.values().forEach { type ->

            var color by remember { mutableStateOf(DarkYellow) }
            var textColor by remember { mutableStateOf(Color.White) }


            color = if (type in selectedFilters) {
                Color.DarkGray.copy(0.7f)
            } else {
                Yellow.copy(0.6f)
            }

            textColor = if (type in selectedFilters) {
                Color.White
            } else {
                Color.Black
            }

            Surface(
                modifier = Modifier
                    .toggleable(value = type in selectedFilters, enabled = true, onValueChange = {

                        if (type in selectedFilters) {
                            selectedFilters.remove(type)
                            color = Yellow.copy(0.6f)
                            textColor = Color.Black
                        } else {
                            selectedFilters.add(type)
                            color = Color.DarkGray.copy(0.7f)
                            textColor = Color.White

                        }

                        onFilter(selectedFilters)
                    },
                    role = Role.Button,
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() })

                   ,
                color = color,
                shape = RoundedCornerShape(40.dp)

            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                ) {

                    Icon(
                        imageVector = type.icon,
                        contentDescription = stringResource(type.title)

                    )

                    Text(text = stringResource(id = type.title), color = textColor)

                }
            }
        }
    }
}