@file:OptIn(ExperimentalFoundationApi::class)

package com.proto.dynamiclayout

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.twotone.CheckCircle
import androidx.compose.material.icons.twotone.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewmodel.compose.viewModel
import com.proto.dynamiclayout.composable.RADButton
import com.proto.dynamiclayout.composable.RADComboBox
import com.proto.dynamiclayout.theme.JetpackComposeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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
    LaunchedEffect(key1 = true) {
        viewModel.registerNetwork()
    }
    viewModel.action.value.apply {
        doNavigation(this)
    }
    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "Home", Modifier.weight(8F))
                        IconButton(onClick = {
                            viewModel.checkInternet()
                        }) {
                            Icon(
                                if (viewModel.isInternetOn.value) Icons.Filled.Wifi else Icons.Filled.WifiOff,
                                "Internet",
                                tint = if (viewModel.isInternetOn.value) Color.Green else Color.Red,

                                )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        doNavigation(Navigation.BACK)
                    }) {
                        Icon(Icons.Filled.ArrowBack, "backIcon")
                    }
                },
                backgroundColor = MaterialTheme.colors.background,
//                contentColor = Color.White,
                elevation = 10.dp
            )
        },
        content = {
            Column {
                GenerateForm(
                    layoutConfig = viewModel.formData,
                    action = { value -> viewModel.doSomething(value) })
//            GenerateList(viewModel.listData) { value -> viewModel.doSomething(value) }
//            GenerateListNew(
//                layoutConfig = viewModel.listDataNew,
//                action = { value -> viewModel.doSomething(value) })
                GenerateConstraintRecord(
                    layoutConfig = viewModel.listDataNew,
                    action = { value -> viewModel.doSomething(value) })
            }
        })

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
fun GenerateConstraintRecord(layoutConfig: MutableList<List<Bloc>>, action: (ACTION) -> Unit) {
    LazyColumn {
        items(items = layoutConfig) { item ->
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                val refs = mutableMapOf<String, ConstrainedLayoutReference>()
                item.forEach { bloc ->
                    val ref = createRef()
                    refs[bloc.uniqueID()] = ref
                    var modifier = Modifier
                        .fillMaxWidth(bloc.ColumnWidth / 100)
                        .height(with(LocalDensity.current) { (bloc.RowHeight + 20).toDp() })
                    modifier = modifier.constrainAs(ref) {
                        if (bloc.DisplayNo == 1 && bloc.LineIndex == 4) {
                            Log.d("TAG", "GenerateConstraintRecord: ")
                        }
                        if (bloc.FieldName == "NearOutOfStock") {
                            refs["${bloc.DisplayNo},${(bloc.LineIndex - 1)}"]?.let { prevRef ->
                                top.linkTo(prevRef.bottom)
                                absoluteLeft.linkTo(prevRef.absoluteLeft)
                            }
                        } else {
                            if (bloc.LineIndex == 1 && bloc.DisplayNo == 1) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                            } else {
                                val prevX = bloc.DisplayNo - 1
                                val prevY = bloc.LineIndex - 1
                                if (prevX == 0) {
                                    recursiveX(refs, prevY).let { prevRef ->
                                        top.linkTo(prevRef.bottom)
                                        absoluteLeft.linkTo(prevRef.absoluteLeft)
                                    }
                                } else if (prevY == 0) {
                                    recursiveY(refs, prevX).let { prevRef ->
                                        top.linkTo(prevRef.top)
                                        start.linkTo(prevRef.end)
                                    }
                                } else {
                                    refs["$prevX,${bloc.LineIndex}"]?.let { prevRef ->
                                        top.linkTo(prevRef.top)
                                        start.linkTo(prevRef.end)
                                    }
                                }
                            }
//                            if (bloc.LineIndex == 1) {
//                                top.linkTo(parent.top)
//                            } else {
//                                refs["${bloc.DisplayNo},${(bloc.LineIndex - 1)}"]?.let { prevRef ->
//                                    top.linkTo(prevRef.bottom)
//                                } ?: run {
//                                    if (bloc.DisplayNo == 1) {
//                                        refs["${bloc.DisplayNo},${(bloc.LineIndex - 2)}"]?.let { prevRef ->
//                                            top.linkTo(prevRef.bottom)
//                                        }
//                                    } else {
//                                        refs["${bloc.DisplayNo - 1},${(bloc.LineIndex)}"]?.let { prevRef ->
//                                            top.linkTo(prevRef.top)
//                                        }
//                                    }
//                                }
//                            }
//                            if (bloc.DisplayNo == 1) {
//                                start.linkTo(parent.start)
//                            } else {
//                                refs["${(bloc.DisplayNo - 1)},${bloc.LineIndex}"]?.let { prevRef ->
//                                    start.linkTo(prevRef.end)
//                                }
//                            }
                        }
                    }
                    GenerateBlocLayout(it = bloc, modifier = modifier, action = action)
                }
            }
        }
    }
}

fun recursiveX(
    refs: MutableMap<String, ConstrainedLayoutReference>,
    prevY: Int
): ConstrainedLayoutReference {
    val ref = refs["1,$prevY"]
    return ref ?: recursiveX(refs, prevY - 1)
}

fun recursiveY(
    refs: MutableMap<String, ConstrainedLayoutReference>,
    prevX: Int
): ConstrainedLayoutReference {
    val ref = refs["$prevX,1"]
    return ref ?: recursiveY(refs, prevX - 1)
}

@Composable
fun GenerateBlocLayout(it: Bloc, modifier: Modifier, action: (ACTION) -> Unit) {
    when (it.FieldControl) {
        "LABEL" -> {
            Text(
                text = it.FieldName,
                modifier = modifier
            )
        }
        "OPTION" -> {
            Text(
                text = it.FieldName,
                modifier = modifier
            )
        }
        "IMAGEWITHPREVIEW", "TAKEPHOTOWITHPREVIEW" -> {
            Text(
                text = it.FieldName,
                modifier = modifier
            )
        }
        "TEXTBOX" -> {
            TextField(
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = modifier,
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
                modifier = modifier,
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