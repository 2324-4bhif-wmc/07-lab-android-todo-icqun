package at.htl.todo.ui.layout

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import at.htl.todo.model.Model
import at.htl.todo.model.ModelStore
import at.htl.todo.model.Todo
import at.htl.todo.ui.theme.TodoTheme
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainView {
    @Inject
    constructor() {
    }

    @Inject
    lateinit var store: ModelStore;

    fun buildContent(activity: ComponentActivity) {
        activity.enableEdgeToEdge()
        activity.setContent {
            val viewModel = store
                .pipe
                .distinctUntilChanged()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeAsState(initial = Model())
                .value

            when (viewModel.uiState.pageIndex) {
                0 -> Overview(model = viewModel, store = store)
                1 -> DetailsView(model = viewModel, store = store)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Overview(model: Model, store: ModelStore?) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(title = {
                Text(text = "ToDos")
            })
        }
    ) { padding ->
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(paddingValues = padding)
        ) {
            Todos(model = model, modifier = Modifier.padding(all = 4.dp), store = store)
        }
    }
}

@Composable
fun Todos(model: Model, modifier: Modifier = Modifier, store: ModelStore?) {
    val todos = model.todos
    LazyColumn(
        modifier = modifier.padding(8.dp)
    ) {
        items(todos.size) { index ->
            TodoRow(todoIdx = index, model = model, store)
            HorizontalDivider()
        }
    }
}

@Composable
fun TodoRow(todoIdx: Int, model: Model, store: ModelStore?) {
    val todo = model.todos[todoIdx];
    val alpha = if(todo.completed) 0.38f else 1f
    val decoration = if(todo.completed) TextDecoration.LineThrough else TextDecoration.None

    ListItem(
        leadingContent = {
            Row (
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = todo.completed,
                    onCheckedChange = {
                        store?.apply { model ->
                            model.todos[todoIdx].completed = it;
                        }
                    }
                )
                CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurface.copy(alpha = alpha)) {
                    Text(
                        text = todo.id.toString(),
                        style = MaterialTheme.typography.labelSmall,
                    )
                }
            }
        },
        headlineContent = {
            CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurface.copy(alpha = alpha)) {
                Text(
                    text = todo.title,
                    style = MaterialTheme.typography.bodyMedium,
                    textDecoration = decoration
                )
            }
        },
        modifier = Modifier.clickable {
            store?.apply { model ->
                val t = model.todos[todoIdx]
                t.completed = !t.completed
            }
        },
        trailingContent = {
            TextButton(
                onClick = {
                      store?.apply { model ->
                          model.uiState.selectedTodoIndex = todoIdx
                          model.uiState.pageIndex = 1
                      }
                },
                shape = CircleShape,
                modifier = Modifier.size(40.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit"
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun TodoPreview() {
    val model = Model()
    val todo = Todo()
    todo.id = 1
    todo.title = "First Todo"
    model.todos = arrayOf(todo)

    TodoTheme {
        Todos(model, store = null)
    }
}