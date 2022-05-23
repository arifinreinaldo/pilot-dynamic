@file:OptIn(ExperimentalFoundationApi::class)

package com.proto.dynamiclayout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewmodel.compose.viewModel
import com.proto.dynamiclayout.composable.RADButton
import com.proto.dynamiclayout.composable.RADComboBox
import com.proto.dynamiclayout.theme.JetpackComposeTheme

class DynamicFragment(val ScreenName: String) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                JetpackComposeTheme {
                    DynamicApp(screenName = ScreenName, doNavigation = {
                        when (it) {
                            Navigation.NEXT -> {
                                next()
                            }
                            Navigation.BACK -> {
                                back()
                            }
                        }
                    })
                }
            }
        }
    }

    fun back() {
        (requireActivity() as MainActivity).back()
    }

    fun next() {
        (requireActivity() as MainActivity).nextScreen("NewPage")
    }
}

enum class Navigation {
    NEXT,
    BACK,
    INIT,
}

@Composable
fun DynamicApp(
    viewModel: DynamicViewModel = viewModel(),
    screenName: String,
    doNavigation: (Navigation) -> Unit,
) {
    viewModel.getLayoutConfig(screenName)
    DynamicScreen(viewModel = viewModel, doNavigation = doNavigation)
}

@Composable
fun DynamicScreen(
    viewModel: DynamicViewModel,
    doNavigation: (Navigation) -> Unit,
) {
    viewModel.action.value.apply {
        doNavigation(this)
    }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Column {
            GenerateForm(
                layoutConfig = viewModel.formData,
                action = { value -> viewModel.doSomething(value) })
//            GenerateList(viewModel.listData) { value -> viewModel.doSomething(value) }
            GenerateListNew(
                layoutConfig = viewModel.listDataNew,
                action = { value -> viewModel.doSomething(value) })
        }
    }
}

@Composable
fun GenerateForm(layoutConfig: MutableList<List<Component>>, action: (ACTION) -> Unit) {
    Column {
        layoutConfig.forEach {
            Row {
                it.forEach {
                    when (it.FieldControl) {
                        "BUTTON" -> {
                            RADButton(value = it.Default, click = { ACTION.EXECUTE(it.FieldName) })
                        }
                        "LABEL" -> {
                            OutlinedTextField(
                                value = it.Default,
                                onValueChange = { it -> })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GenerateListNew(layoutConfig: MutableList<List<Bloc>>, action: (ACTION) -> Unit) {
    LazyColumn {
        items(items = layoutConfig) { item ->
            for (i in 1..item.last().LineIndex) {
                item.filter { it.LineIndex == i }.apply {
                    Row {
                        forEach {
                            if (it.ColumnWidth > 0) {
                                when (it.FieldControl) {
                                    "LABEL" -> {
                                        Text(
                                            text = it.FieldName,
                                            modifier = Modifier.weight(it.ColumnWidth, true)
                                        )
                                    }
                                    "IMAGEWITHPREVIEW", "TAKEPHOTOWITHPREVIEW" -> {
                                        Text(
                                            text = it.FieldName,
                                            modifier = Modifier.weight(it.ColumnWidth, true)
                                        )
                                    }
                                    "TEXTBOX" -> {
                                        TextField(
                                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                                            modifier = Modifier.weight(it.ColumnWidth, true),
                                            value = it.value.value,
                                            onValueChange = { data ->
                                                action(
                                                    ACTION.TEXTCHANGE(
                                                        data,
                                                        "UPDATE",
                                                        it.id
                                                    )
                                                )
                                            })
                                    }
                                    "COMBOBOX" -> {
                                        RADComboBox(
                                            modifier = Modifier.weight(
                                                it.ColumnWidth,
                                                true,
                                            ),
                                            it.FieldName,
                                            listOf("1", "2", "3"),
                                            { data ->
                                                action(
                                                    ACTION.TEXTCHANGE(
                                                        data,
                                                        "UPDATE",
                                                        it.id
                                                    )
                                                )
                                            },
                                            it.value.value
                                        )
                                    }
                                    "OPTION" -> {

                                    }
                                }
                            }
                        }
                    }
                }
            }
//            GenerateListConfigRecord(element = listOf(it), action = action)
        }
    }
}

@Composable
fun GenerateList(layoutConfig: MutableList<Component>, action: (ACTION) -> Unit) {
    LazyColumn {
        items(items = layoutConfig) {
            GenerateListConfigRecord(element = listOf(it), action = action)
        }
    }
}

@Composable
fun GenerateListConfigRecord(element: List<Component>, action: (ACTION) -> Unit) {
    Row(
        Modifier
            .combinedClickable(onClick = {
                ACTION.EXECUTE("Short")
            }, onLongClick = {
                ACTION.EXECUTE("Long")
            })
            .padding(10.dp)
    ) {
        element.forEach {
            when (it.FieldControl) {
                "BUTTON" -> {
                    RADButton(value = it.Default, click = { ACTION.EXECUTE(it.FieldName) })
                }
                "LABEL" -> {
                    Text(it.Default)
//                    OutlinedTextField(value = it.Default, onValueChange = { it -> })
                }
            }
        }
    }
}