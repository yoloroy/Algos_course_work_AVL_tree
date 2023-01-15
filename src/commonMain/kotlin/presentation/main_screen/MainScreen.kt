package presentation.main_screen

import Global
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.useResource
import androidx.compose.ui.unit.dp
import com.yoloroy.loadAvlTree
import com.yoloroy.loadTree
import com.yoloroy.saveAvlTree
import com.yoloroy.saveTree
import presentation.avl_tree_editor.AvlTreeEditorScreen
import presentation.main_screen.Page.*
import presentation.util.openLoadFileDialog
import presentation.util.openSaveFileDialog
import java.io.File

private val START_PAGE = TreeEditor

@Preview
@Composable
fun MainScreen() { // TODO refactor
    val scaffoldState = rememberScaffoldState()
    var page by remember {
        mutableStateOf(
            try {
                val lastPage = useResource("config/last_page.txt") { it.readAllBytes().contentToString() }
                Page.valueOf(lastPage)
            } catch (e: IllegalArgumentException) {
                START_PAGE
            }
        )
    }

    var avlTree by remember {
        mutableStateOf(
            loadAvlTree(
                ClassLoader.getSystemResourceAsStream("sample_trees/sample_avl_tree.avl_tree.json")!!,
                String::toInt
            ),
            neverEqualPolicy()
        )
    }
    val reloadAvlTree = {
        @Suppress("SelfAssignment")
        avlTree = avlTree
    }
    var tree by remember {
        mutableStateOf(
            loadTree(ClassLoader.getSystemResourceAsStream("sample_trees/sample_tree.tree.json")!!),
            neverEqualPolicy()
        )
    }
    val reloadTree = {
        @Suppress("SelfAssignment")
        tree = tree
    }

    fun onClickSaveButton() { // TODO refactor move logic from here
        val chosenFile = runSaveTreeDialog() ?: return
        when (page) {
            AvlTreeEditor -> {
                val file = File(chosenFile.absolutePath + ".avl_tree.json")
                saveAvlTree(avlTree, file)
            }
            TreeEditor -> {
                val file = File(chosenFile.absolutePath + ".tree.json")
                saveTree(tree, file)
            }
            else -> Unit
        }
    }

    fun onClickLoadButton() { // TODO refactor move logic from here
        val extension = when (page) {
            AvlTreeEditor -> ".avl_tree.json"
            TreeEditor -> ".tree.json"
            else -> return
        }
        val file = runLoadTreeDialog(extension) ?: return
        when (page) {
            AvlTreeEditor -> avlTree = loadAvlTree(file, String::toInt)
            TreeEditor -> tree = loadTree(file)
            else -> Unit
        }
    }

    var bottomBarHeightPx by remember { mutableStateOf(0) }
    val bottomBarHeight = with (LocalDensity.current) { bottomBarHeightPx.toDp() }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        scaffoldState = scaffoldState,
        bottomBar = {
            BottomBar(
                page = page,
                gotoPage = { new -> page = new },
                updateBottomBarHeightPx = { new -> bottomBarHeightPx = new },
                onClickSaveButton = ::onClickSaveButton,
                onClickLoadButton = ::onClickLoadButton
            )
        },
        content = {
            BoxWithConstraints (
                modifier = Modifier
                    .background(MaterialTheme.colors.background)
                    .fillMaxSize()
                    .padding(bottom = bottomBarHeight)
            ) {
                when (page) {
                    Info -> InfoScreen()
                    TreeEditor -> TreeEditorScreen(tree, reloadTree)
                    AvlTreeEditor -> AvlTreeEditorScreen(avlTree, reloadAvlTree)
                }
            }
        }
    )
}

@Composable
private fun BottomBar(
    page: Page,
    gotoPage: (Page) -> Unit,
    updateBottomBarHeightPx: (Int) -> Unit,
    onClickSaveButton: () -> Unit,
    onClickLoadButton: () -> Unit
) {
    LazyRow(
        modifier = Modifier
            .background(MaterialTheme.colors.primarySurface)
            .fillMaxWidth()
            .onSizeChanged { updateBottomBarHeightPx(it.height) },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        contentPadding = PaddingValues(horizontal = 20.dp)
    ) {
        item { BottomBarPageButtons(gotoPage) }
        item { BottomBarSaveAndLoadTreeButtons(page, onClickSaveButton, onClickLoadButton) }
    }
}

@Composable
private fun BottomBarPageButtons(setPage: (Page) -> Unit) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TextButton(onClick = { setPage(Info) }) {
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = "Info",
                tint = MaterialTheme.colors.onPrimary,
                modifier = Modifier.size(36.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Справка",
                color = MaterialTheme.colors.onPrimary
            )
        }
        Divider(
            modifier = Modifier
                .width(1.dp)
                .height(24.dp),
            color = MaterialTheme.colors.onPrimary.copy(alpha = 0.4f)
        )
        TextButton(
            onClick = { setPage(TreeEditor) },
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Icon(
                painter = painterResource("icons/rebase_edit.svg"),
                contentDescription = "Tree editor",
                tint = MaterialTheme.colors.onPrimary,
                modifier = Modifier.size(36.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Бинарное дерево",
                color = MaterialTheme.colors.onPrimary
            )
        }
        TextButton(onClick = { setPage(AvlTreeEditor) }) {
            Icon(
                painter = painterResource("icons/binary-search-tree.svg"),
                contentDescription = "Avl Tree editor",
                tint = MaterialTheme.colors.onPrimary,
                modifier = Modifier.size(36.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "АВЛ-дерево",
                color = MaterialTheme.colors.onPrimary
            )
        }
    }
}

@Composable
private fun BottomBarSaveAndLoadTreeButtons(
    page: Page,
    onClickSaveButton: () -> Unit,
    onClickLoadButton: () -> Unit
) {
    if (page !in listOf(TreeEditor, AvlTreeEditor)) return
    Row(
        modifier = Modifier.padding(start = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Divider(
            modifier = Modifier
                .width(1.dp)
                .height(24.dp),
            color = MaterialTheme.colors.onPrimary.copy(alpha = 0.4f)
        )
        TextButton(onClick = onClickSaveButton) {
            Icon(
                painter = painterResource("icons/save_as.svg"),
                contentDescription = "Save tree",
                tint = MaterialTheme.colors.onPrimary,
                modifier = Modifier.size(36.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Сохранить дерево",
                color = MaterialTheme.colors.onPrimary
            )
        }
    }
    TextButton(onClick = onClickLoadButton) {
        Icon(
            painter = painterResource("icons/folder_open.svg"),
            contentDescription = "Load tree",
            tint = MaterialTheme.colors.onPrimary,
            modifier = Modifier.size(36.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = "Загрузить дерево",
            color = MaterialTheme.colors.onPrimary
        )
    }
}

context(BoxWithConstraintsScope)
@Composable
private fun InfoScreen() {
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
                text = useResource("config/help.txt") { it.readAllBytes().decodeToString() },
                modifier = Modifier.padding(
                    start = oneCentimeter * 2,
                    end = oneCentimeter
                )
            )
            Spacer(modifier = Modifier.height(oneCentimeter))
        }
    }
}

private fun runSaveTreeDialog(): File? =
    openSaveFileDialog(Global.window, "Сохранить дерево", listOf(""), false).firstOrNull()

private fun runLoadTreeDialog(extension: String): File? =
    openLoadFileDialog(Global.window, "Загрузить дерево", listOf(extension), false).firstOrNull()

enum class Page { Info, TreeEditor, AvlTreeEditor }
