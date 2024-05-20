package at.htl.todo.ui.layout

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import at.htl.todo.model.Model
import at.htl.todo.model.ModelStore
import at.htl.todo.model.Todo
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import java.time.format.TextStyle
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DetailsView {

    @Inject
    constructor() {
    }

    @Inject
    lateinit var store: ModelStore;

    fun buildContent(activity: ComponentActivity, todoId: Long) {
        activity.enableEdgeToEdge()
        activity.setContent {
            val viewModel = store
                .pipe
                .flatMap { Observable.fromIterable(it.todos.asIterable()) }
                .filter { it.id == todoId }
                .distinctUntilChanged()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeAsState(initial = null)
                .value

            if (viewModel == null) {
                return@setContent Surface {}
            }

            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = { DetailsTopBar(todo = viewModel, activity = activity) }
            ) { innerPadding ->
                Details(
                    viewModel,
                    Modifier
                        .padding(innerPadding)
                        .fillMaxSize(), store
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsTopBar(todo: Todo, activity: ComponentActivity) {
    CenterAlignedTopAppBar(title = {
        Text(text = "Edit Todo")
    }, navigationIcon = {
        IconButton(onClick = { activity.onBackPressedDispatcher.onBackPressed() }) {
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
        }
    })
}

@Composable
fun Details(todo: Todo, modifier: Modifier = Modifier, store: ModelStore) {
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
            value = todo.title,
            onValueChange = {
                store.apply { model ->
                    model.todos.find {
                        it.id == todo.id
                    }?.title = it
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
            value = todo.userId.toString(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            onValueChange = {
                store.apply { model ->
                    val t = model.todos.find {
                        it.id == todo.id
                    }

                    if (it.isEmpty()) {
                        t?.userId = 0
                    } else if (it.toLongOrNull() != null) {
                        t?.userId = it.toLong()
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
                    store.apply { model ->
                        model.todos.find {
                            it.id == todo.id
                        }?.completed = it;
                    }
                }
            )
        }
    }
}
