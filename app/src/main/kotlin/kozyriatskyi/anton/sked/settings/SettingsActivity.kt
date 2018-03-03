package kozyriatskyi.anton.sked.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kozyriatskyi.anton.sked.R

class SettingsActivity : AppCompatActivity() {

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, SettingsActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        supportFragmentManager.beginTransaction()
                .replace(R.id.settings_layout_container, SettingsFragment(), SettingsFragment.TAG)
                .commit()
    }
}
