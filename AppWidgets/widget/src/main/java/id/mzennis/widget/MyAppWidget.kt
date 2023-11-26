package id.mzennis.widget

import android.content.Context
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.text.FontStyle
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle

class MyAppWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            // create your AppWidget here
            GlanceTheme {
                Box(
                    modifier = GlanceModifier.fillMaxSize()
                        .background(GlanceTheme.colors.background)
                        .appWidgetBackground(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "Glance Widget!",
                        modifier = GlanceModifier.padding(16.dp),
                        style = TextStyle(
                            fontSize = 24.sp,
                            fontStyle = FontStyle.Italic,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}