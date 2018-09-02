package kozyriatskyi.anton.sked.audiences

import android.support.annotation.ColorRes

data class AudienceUi(val number: String,
                      val isFree: Boolean,
                      val status: String,
                      val note: String,
                      val capacity: String,
                      @ColorRes val colorId: Int)