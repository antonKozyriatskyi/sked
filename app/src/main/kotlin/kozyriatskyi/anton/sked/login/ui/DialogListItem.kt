package kozyriatskyi.anton.sked.login.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kozyriatskyi.anton.sked.util.Callback

@Composable
fun DialogListItem(
    item: String,
    onClick: Callback
) {
    Text(
        text = item,
        modifier = Modifier
            .clickable(
                onClick = onClick,
                indication = rememberRipple(bounded = true),
                interactionSource = remember { MutableInteractionSource() },
            )
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .fillMaxWidth()
    )
}

