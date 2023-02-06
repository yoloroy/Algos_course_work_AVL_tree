package presentation.info

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

context(BoxWithConstraintsScope)
@Composable
fun InfoScreen() {
    val pageWidth = 640.dp
    val pseudoCentimetersWidth = 21
    val oneCentimeter = pageWidth / pseudoCentimetersWidth
    Surface(
        modifier = Modifier
            .align(Alignment.TopCenter)
            .fillMaxHeight()
            .verticalScroll(rememberScrollState())
            .widthIn(max = pageWidth)
            .run {
                if (maxWidth >= pageWidth + oneCentimeter * 4) {
                    padding(horizontal = oneCentimeter * 2)
                } else {
                    this
                }
            },
        elevation = 2.dp
    ) {
        Column {
            Spacer(modifier = Modifier.height(oneCentimeter))
            Text(
                text = StringRes.text.help,
                modifier = Modifier
                    .padding(
                        start = oneCentimeter * 2,
                        end = oneCentimeter
                    )
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(oneCentimeter))
        }
    }
}
