package devolab.projects.babilejo.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Normal
import androidx.compose.ui.unit.sp
import devolab.projects.babilejo.R


val quicksand = FontFamily(
    Font(R.font.quicksand_regular),
    Font(R.font.quicksand_bold, FontWeight.Bold),
    Font(R.font.quicksand_light, FontWeight.Thin),
    Font(R.font.quicksand_medium, FontWeight.Medium),
    Font(R.font.quicksand_semibold, FontWeight.SemiBold)
)

// Set of Material typography styles to start with
val Typography = Typography(
    body1 = TextStyle(
        fontFamily = quicksand,
        fontWeight = Normal,
        fontSize = 16.sp
    ),
    button = TextStyle(
        fontFamily = quicksand,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = quicksand,
        fontWeight = Normal,
        fontSize = 12.sp
    )

)