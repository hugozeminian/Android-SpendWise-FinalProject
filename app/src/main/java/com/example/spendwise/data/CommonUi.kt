package com.example.spendwise.data

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.spendwise.R
import com.example.spendwise.ui.theme.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDropdownMenu(
    list: List<String>,
    modifier: Modifier = Modifier
): String{

    var isExpanded by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf(list[0]) }

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = { isExpanded = !isExpanded}
    ) {
        TextField(
            modifier = Modifier
                .menuAnchor()
                .width(180.dp),
            value = selectedItem,
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
            }
        )

        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false }
        ) {
            list.forEachIndexed { index, text ->
                DropdownMenuItem(
                    text = { Text(text = text) },
                    onClick = {
                        selectedItem = list[index]
                        isExpanded = false },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }

    return selectedItem //This function returns the item selected
}

@Composable
fun NumericAlertMessage(showAlertMessage: Boolean) {
    if (showAlertMessage) {
        Text(
            text = stringResource(id = R.string.app_keyboard_number_alert),
            color = Color.Red,
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}
