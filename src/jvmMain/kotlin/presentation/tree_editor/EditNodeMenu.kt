package presentation.tree_editor

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yoloroy.algorithm_mod.children

@Composable
fun EditNodeMenu(
    state: EditNodeMenuState,
    onDismissRequest: () -> Unit,
    onClickDelete: () -> Unit = {},
    onClickEdit: () -> Unit = {},
    onClickSwapWithParent: () -> Unit = {},
    onClickSwapWithLeftChild: () -> Unit = {},
    onClickSwapWithRightChild: () -> Unit = {},
    onClickSwapChildren: () -> Unit = {},
    onClickMoveRight: () -> Unit = {},
    onClickMoveLeft: () -> Unit = {}
) {
    CursorDropdownMenu(
        expanded = state is EditNodeMenuState.Expanded,
        onDismissRequest = onDismissRequest
    ) {
        if (state !is EditNodeMenuState.Expanded) return@CursorDropdownMenu
        Text(
            text = state.node.value.toString(),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 8.dp),
            style = MaterialTheme.typography.h6
        )
        Divider(modifier = Modifier.fillMaxWidth())
        DropdownMenuItem(
            onClick = onClickEdit
        ) {
            Text("Изменить значение")
        }
        if (state.node.parent != null) {
            DropdownMenuItem(
                onClick = onClickDelete
            ) {
                Text("Удалить узел")
            }
            DropdownMenuItem(
                onClick = onClickSwapWithParent
            ) {
                Text("Поменяться значениями с родительским узлом")
            }
        }
        if (state.node.left != null) {
            DropdownMenuItem(
                onClick = onClickSwapWithLeftChild
            ) {
                Text("Поменяться значениями с левым дочерним узлом")
            }
        }
        if (state.node.right != null) {
            DropdownMenuItem(
                onClick = onClickSwapWithRightChild
            ) {
                Text("Поменяться значениями с правым дочерним узлом")
            }
        }
        if (state.node.children.isNotEmpty()) {
            DropdownMenuItem(
                onClick = onClickSwapChildren
            ) {
                Text("Поменять местами поддеревья")
            }
        }
        if (state.node.left != null) {
            DropdownMenuItem(
                onClick = onClickMoveRight
            ) {
                Text("Сдвинуть вправо")
            }
        }
        if (state.node.right != null) {
            DropdownMenuItem(
                onClick = onClickMoveLeft
            ) {
                Text("Сдвинуть влево")
            }
        }
    }
}
