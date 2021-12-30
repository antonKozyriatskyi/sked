package kozyriatskyi.anton.sked.screen

import androidx.compose.runtime.Composable
import com.google.android.material.composethemeadapter.MdcTheme
import kozyriatskyi.anton.sked.flow.root.RootFlow

@Composable
fun SkedApp() {
    MdcTheme {
        RootFlow()
    }
}