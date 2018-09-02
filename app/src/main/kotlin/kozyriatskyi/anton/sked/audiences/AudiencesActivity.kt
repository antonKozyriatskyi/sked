package kozyriatskyi.anton.sked.audiences

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kozyriatskyi.anton.sked.R
import kozyriatskyi.anton.sked.data.pojo.Item
import javax.inject.Inject

class AudiencesActivity : AppCompatActivity(), AudiencesView {

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, AudiencesActivity::class.java))
        }
    }

    @Inject
    @InjectPresenter
    lateinit var presenter: AudiencesPresenter

    @ProvidePresenter
    fun providePresenter(): AudiencesPresenter {
//        Injector. // TODO
        return presenter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_free_audiences)
    }

    override fun showAudiences(audiences: List<AudienceUi>) {
        TODO("not implemented")
    }

    override fun showErrorLoadingAudiences() {
        TODO("not implemented")
    }

    override fun showTimes(start: List<Item>, end: List<Item>) {
        TODO("not implemented")
    }

    override fun showErrorLoadingTimes() {
        TODO("not implemented")
    }
}