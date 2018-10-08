package kozyriatskyi.anton.sked.audiences

import android.support.annotation.DrawableRes

data class AudienceUi(val number: String,
                      val isFree: Boolean,
                      val status: String,
                      val note: String,
                      val capacity: String,
                      @DrawableRes val iconId: Int)