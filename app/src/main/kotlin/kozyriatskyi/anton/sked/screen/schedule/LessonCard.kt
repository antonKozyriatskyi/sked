package kozyriatskyi.anton.sked.screen.schedule

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import kozyriatskyi.anton.sked.data.pojo.LessonUi
import kozyriatskyi.anton.sked.util.ValueCallback

@Composable
fun LessonCard(lesson: LessonUi, onClick: ValueCallback<LessonUi>, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .clickable { onClick(lesson) }
            .padding(all = 8.dp)

    ) {
        Column {
            Row {
                Text(lesson.name)
                Text(lesson.type, color = colorResource(lesson.typeColorRes))
            }

            Row {
                Text(lesson.number)
                Text(lesson.time)
            }

            Row {
                Text(lesson.who)
                Text(lesson.cabinet)
            }
        }
    }
}