package kozyriatskyi.anton.sked.login.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kozyriatskyi.anton.sked.data.pojo.Item
import kozyriatskyi.anton.sked.ui.Interaction
import kozyriatskyi.anton.sked.util.ValueCallback
import kozyriatskyi.anton.sked.util.rememberMutableState

@Composable
fun LoginItemSelector(
    title: String,
    items: List<Item>,
    selectedItem: Item?,
    enable: Boolean = true,
    onItemSelected: ValueCallback<Item>,
) {
    var openSelectionDialog by rememberMutableState(false)

    Interaction(enable) {
        Column {
            Text(
                text = title,
                modifier = Modifier.padding(horizontal = 16.dp),
            )

            SelectedLoginItem(
                value = selectedItem?.value,
                onClick = { openSelectionDialog = !openSelectionDialog },
                modifier = Modifier.padding(horizontal = 16.dp),
            )

            if (openSelectionDialog) {
                Dialog(
                    onDismissRequest = { openSelectionDialog = false }
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        LazyColumn(
                            contentPadding = PaddingValues(vertical = 8.dp)
                        ) {
                            items(items) {
                                DialogListItem(
                                    item = it.value,
                                    onClick = {
                                        onItemSelected(it)
                                        openSelectionDialog = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}