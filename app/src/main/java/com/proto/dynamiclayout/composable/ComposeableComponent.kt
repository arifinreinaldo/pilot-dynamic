package com.proto.dynamiclayout.composable

import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun RADButton(
    modifier: Modifier = Modifier,
    value: String,
    click: () -> Unit
) {
    Button(
        modifier = modifier,
        onClick = click
    ) {
        Text(value)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RADComboBox(
    modifier: Modifier,
    label: String,
    options: List<String>,
    onValueChange: (String) -> Unit,
    value: String
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {
        TextField(
            readOnly = true,
            value = value,
            onValueChange = { },
            label = { Text(label) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    onClick = {
                        expanded = false
                        onValueChange(selectionOption)
                    }
                ) {
                    Text(text = selectionOption)
                }
            }
        }
    }
}