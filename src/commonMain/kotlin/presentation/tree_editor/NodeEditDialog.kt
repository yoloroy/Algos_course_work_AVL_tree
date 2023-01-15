package presentation.tree_editor

import StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NodeEditDialog(
    dialogData: NodeEditDialogData
) {
    if (!dialogData.open) return
    AlertDialog(
        onDismissRequest = dialogData.onClickDismiss,
        title = { Title(dialogData) },
        text = { Body(dialogData) },
        buttons = { Buttons(dialogData) }
    )
}

@Composable
private fun Title(dialogData: NodeEditDialogData) {
    Text(
        text = dialogData.title + '\n',
        style = MaterialTheme.typography.h6,
    )
}

@Composable
private fun Body(dialogData: NodeEditDialogData) {
    OutlinedTextField(
        value = dialogData.inputText,
        onValueChange = dialogData.updateValue,
        label = { Text(dialogData.label) },
        modifier = Modifier.padding(horizontal = 12.dp)
    )
}

@Composable
private fun Buttons(dialogData: NodeEditDialogData) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
        modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp)
    ) {
        Spacer(modifier = Modifier.weight(1f))
        TextButton(
            onClick = dialogData.onClickDismiss
        ) {
            Text(StringRes.ui.treeActions.dialogLabel.cancel)
        }
        Button(
            onClick = dialogData.onClickOk,
            enabled = dialogData.isOk ?: false
        ) {
            Text(StringRes.ui.treeActions.dialogLabel.ok)
        }
    }
}
