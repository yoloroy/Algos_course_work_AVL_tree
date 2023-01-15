package presentation.tree_editor

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import presentation.tree_editor.EditNodeMenuState.Expanded
import presentation.tree_editor.EditNodeMenuState.ExpandedWithNoNode

@Composable
fun <T> EditNodeMenu(
    state: EditNodeMenuState<T>,
    onDismissRequest: () -> Unit,
    menuItems: Iterable<EditNodeMenuItem>
) {
    CursorDropdownMenu(
        expanded = state is Expanded,
        onDismissRequest = onDismissRequest
    ) {
        if (state is EditNodeMenuState.Hidden) return@CursorDropdownMenu
        Text(
            text = when (state) {
                is Expanded<*> -> state.nodeName
                is ExpandedWithNoNode<*> -> state.label
                else -> throw IllegalStateException("this cannot happen")
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 8.dp),
            style = MaterialTheme.typography.h6
        )
        Divider(modifier = Modifier.fillMaxWidth())
        for (menuItem in menuItems) {
            DropdownMenuItem(onClick = menuItem.onClick) {
                Text(menuItem.name)
            }
        }
    }
}

data class EditNodeMenuItem(val name: String, val onClick: () -> Unit)
