package resources.icons

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.intl.Locale

@Composable
fun localizedPainterResource(name: String, locale: Locale = Locale.current) =
    painterResource(name.replace(".svg", "-${locale.language}.svg")) // TODO? create dictionary for these paths
