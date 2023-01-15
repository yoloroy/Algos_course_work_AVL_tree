package presentation.tree_editor

data class NodeEditDialogData(
    val open: Boolean,
    val title: String,
    val label: String,
    val inputText: String,
    val isOk: Boolean?,
    val onClickDismiss: () -> Unit,
    val onClickOk: () -> Unit,
    val updateValue: (String) -> Unit
)
