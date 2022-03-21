package kozyriatskyi.anton.sked.login

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kozyriatskyi.anton.sked.R
import kozyriatskyi.anton.sked.util.Callback
import kozyriatskyi.anton.sked.util.ComposableBody

/**
 * Created by Backbase R&D B.V. on 20.03.2022.
 */
@Composable
fun LoginScreenScaffold(
    showConnectionError: Boolean,
    enableButton: Boolean,
    onButtonClick: Callback,
    content: ComposableBody
) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        scaffoldState = scaffoldState
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier.weight(1f)
            ) {
                content()
            }

            Button(
                enabled = enableButton,
                onClick = onButtonClick,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.secondary,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 16.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.login_load_schedule),
                    style = MaterialTheme.typography.button
                )
            }
        }
    }

    if (showConnectionError) {
        val message = stringResource(id = R.string.login_no_connection)
        LaunchedEffect(Unit) {
            scaffoldState.snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Indefinite
            )
        }
    }
}