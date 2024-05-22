package at.htl.todo.ui.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import at.htl.todo.model.Model
import at.htl.todo.model.Model.UIState
import at.htl.todo.model.ModelStore
import at.htl.todo.model.Todo
import at.htl.todo.ui.theme.TodoTheme

@Composable
fun DetailsView(model: Model,  store: ModelStore?) {
    if (model.uiState.selectedTodoIndex >= model.todos.size || model.uiState.selectedTodoIndex < 0) {
        return Surface {}
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { DetailsTopBar(store) }
    ) { innerPadding ->
        Details(
            model,
            Modifier
                .padding(innerPadding)
                .fillMaxSize(), store
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsTopBar(store: ModelStore?) {
    CenterAlignedTopAppBar(title = {
        Text(text = "Edit Todo")
    }, navigationIcon = {
        IconButton(onClick = {
            store?.apply { model ->
                model.uiState.pageIndex = 0
                model.uiState.selectedTodoIndex = -1
            }
        }) {
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
        }
    })
}

@Composable
fun Details(model: Model, modifier: Modifier = Modifier, store: ModelStore?) {
    val todo = model.todos[model.uiState.selectedTodoIndex]
    val title = remember { mutableStateOf(model.todos[model.uiState.selectedTodoIndex].title)}
    val id = remember { mutableStateOf(model.todos[model.uiState.selectedTodoIndex].id)}

    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top,
        modifier = modifier.padding(30.dp)
    ) {
        Text(
            text = "Id",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleSmall
        )
        Spacer(modifier = Modifier.padding(5.dp))
        Text(text = "${todo.id}", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.padding(10.dp))
        Text(
            text = "Title",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleSmall
        )
        Spacer(modifier = Modifier.padding(5.dp))
        OutlinedTextField(
            value = title.value,
            onValueChange = {
                title.value = it
                store?.apply { model ->
                    model.todos[model.uiState.selectedTodoIndex].title = it
                }
            }
        )
        Spacer(modifier = Modifier.padding(10.dp))
        Text(
            text = "User Id",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleSmall
        )
        Spacer(modifier = Modifier.padding(5.dp))
        OutlinedTextField(
            value = id.value.toString(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            onValueChange = {
                if (it.isEmpty()) {
                    id.value = 0
                    store?.apply { model ->
                        model.todos[model.uiState.selectedTodoIndex].userId = 0
                    }
                } else if (it.toLongOrNull() != null) {
                    id.value = it.toLong()
                    store?.apply { model ->
                        model.todos[model.uiState.selectedTodoIndex].userId = it.toLong()
                    }
                }
            }
        )
        Spacer(modifier = Modifier.padding(10.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Completed",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(modifier = Modifier.padding(5.dp))
            Checkbox(
                checked = todo.completed,
                onCheckedChange = {
                    store?.apply { model ->
                        model.todos[model.uiState.selectedTodoIndex].completed = it;
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailsPreview() {
    val model = Model()
    val uiState = UIState()
    uiState.pageIndex = 1
    uiState.selectedTodoIndex = 0
    val todo = Todo()
    todo.id = 1
    todo.title = "First Todo"
    model.todos = arrayOf(todo)


    TodoTheme {
        DetailsView(model, null)
    }
}
