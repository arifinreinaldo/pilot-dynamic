package com.proto.dynamiclayout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewmodel.compose.viewModel
import com.proto.dynamiclayout.theme.JetpackComposeTheme
import kotlin.random.Random

class ComposeFragment(private val count: Int) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                JetpackComposeTheme {
                    MyApp()
                }
            }
        }
    }

    fun next() {
        (requireActivity() as MainActivity).nextScreen(Random.nextInt(1, 10))
    }
}

@Composable
fun MyApp(
    names: List<String> = listOf("World", "Compose"),
    wellnessViewModel: ComposeViewModel = viewModel()
) {
//    var shouldShowOnboarding by rememberSaveable { mutableStateOf(true) }
//    if (shouldShowOnboarding) {
//        OnboardingScreen { shouldShowOnboarding = false }
//    } else {
//        LazyGreetings(names = names)
//    }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.primary
    ) {
        Column {
            WellnessScreen()

            WellnessTasksList(
                list = wellnessViewModel.tasks,
                onCloseTask = { tas -> wellnessViewModel.remove(tas) },
                onChange = { task, bool -> wellnessViewModel.changeTaskChecked(task, bool) }
            )
        }
    }

}

@Composable
fun LazyGreetings(names: List<String>) {
    val dummy = mutableListOf<String>()
    for (i in 1..100) {
        dummy.addAll(names)
    }
    LazyColumn(Modifier.padding(4.dp)) {
        items(items = dummy) { item ->
            Greeting(name = item)
        }
    }
}

@Composable
fun Greetings(names: List<String>) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        for (name in names) {
            Greeting(name = name)
        }
    }
}

@Composable
private fun Greeting(name: String) {
    val expanded = remember { mutableStateOf(false) }
    val extraPadding by animateDpAsState(targetValue = if (expanded.value) 48.dp else 0.dp)
    Surface(
        color = MaterialTheme.colors.primary,
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        Row(Modifier.padding(24.dp)) {
            Column(
                modifier = Modifier
                    .weight(1F)
                    .padding(bottom = extraPadding)
            ) {
                Text(text = "Hello, ")
                Text(text = name)
            }
            OutlinedButton(onClick = { expanded.value = !expanded.value }) {
                Text(if (expanded.value) "Show less" else "Show more")
            }
        }
    }
}


@Composable
fun OnboardingScreen(click: () -> Unit) {

    Surface {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Welcome to the Basics Codelab!")
            Button(
                modifier = Modifier.padding(vertical = 24.dp),
                onClick = click
            ) {
                Text("Continue")
            }
        }
    }
}


@Composable
fun WaterCounter(modifier: Modifier = Modifier, count: Int, onChange: (Int) -> Unit) {
    Column(modifier = modifier.padding(16.dp)) {
        if (count > 0) {
            var showTask by remember { mutableStateOf(true) }
            if (showTask) {
                WellnessTaskItem(
                    onClose = { showTask = false },
                    taskName = "Have you taken your 15 minute walk today?",
                    onChange = {},
                    modifier = modifier,
                    checked = false
                )
            }

            Text(
                text = "You've had $count glasses.",
                modifier = modifier.padding(16.dp)
            )
        }
        Row {
            Button(onClick = { onChange(count + 1) }) {
                Text("Add one")
            }
            Button(onClick = { onChange(0) }) {
                Text("Clear water count")
            }

        }
    }

}

@Composable
fun WellnessScreen(
    modifier: Modifier = Modifier
) {
    var count by remember { mutableStateOf(0) }
    WaterCounter(modifier, count) { count = it }
}

@Composable
fun WellnessTaskItemStateful(taskName: String, modifier: Modifier = Modifier, onClose: () -> Unit) {
    var checkedState by rememberSaveable { mutableStateOf(false) }//to make sure does not recreate on lazy
    WellnessTaskItem(
        taskName = taskName,
        checked = checkedState,
        onChange = { newValue -> checkedState = newValue },
        onClose = onClose, // we will implement this later!
        modifier = modifier,
    )
}

@Composable
fun WellnessTaskItem(
    taskName: String,
    checked: Boolean,
    onChange: (Boolean) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier, verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp),
            text = taskName
        )
        Checkbox(
            checked = checked,
            onCheckedChange = onChange
        )

        IconButton(onClick = onClose) {
            Icon(Icons.Filled.Close, contentDescription = "Close")
        }
    }
}

@Composable
fun WellnessTasksList(
    modifier: Modifier = Modifier,
    list: List<WellnessTask>,
    onCloseTask: (WellnessTask) -> Unit,
    onChange: (WellnessTask, Boolean) -> Unit,
) {
    LazyColumn(
        modifier = modifier,
        state = rememberLazyListState()
    ) {
        items(list) { task ->
//            WellnessTaskItemStateful(taskName = task.label, onClose = { onCloseTask(task) })
            WellnessTaskItem(
                taskName = task.label,
                checked = task.checked,
                onClose = { onCloseTask(task) },
                onChange = { b -> onChange(task, b) })
        }
    }
}
