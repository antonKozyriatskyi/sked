package kozyriatskyi.anton.sked.about

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.ComposeView
import com.google.android.material.composethemeadapter.MdcTheme

class AboutActivity : AppCompatActivity() {

    companion object {

        fun start(context: Context) {
            context.startActivity(Intent(context, AboutActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(ComposeView(this).apply {
            setContent {
                MdcTheme {
                    AboutScreen(
                        onNavigateUp = { finish() }
                    )
                }
            }
        })
    }
}

