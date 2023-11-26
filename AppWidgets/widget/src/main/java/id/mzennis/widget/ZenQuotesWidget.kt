package id.mzennis.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.action.ActionParameters
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.FontStyle
import androidx.glance.text.Text
import androidx.glance.text.TextStyle

class ZenQuotesWidget : GlanceAppWidget() {

    override val stateDefinition: GlanceStateDefinition<*> = PreferencesGlanceStateDefinition

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {

            val preferences = currentState<Preferences>()
            val currentQuote = preferences[quoteKey] ?: staticQuotes.random()

            ZenQuotesWidgetUi(currentQuote)
        }
    }
}

@Composable
fun ZenQuotesWidgetUi(currentQuote: String) {
    GlanceTheme {
        Box(
            modifier = GlanceModifier.fillMaxSize()
                .background(GlanceTheme.colors.background)
                .appWidgetBackground()
                .clickable(actionRunCallback<RefreshQuoteAction>()),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = currentQuote,
                modifier = GlanceModifier.padding(16.dp),
                style = TextStyle(
                    fontSize = 16.sp,
                    fontStyle = FontStyle.Italic
                )
            )
        }
    }
}

/*
* credit: https://victorbrandalise.com/building-a-widget-using-jetpack-glance/
* todo, get quotes from https://zenquotes.io/api/random
*/
private val staticQuotes = listOf(
    "Life has no limitations, except the ones you make.  ― Les Brown",
    "Be curious about everything. Never stop learning. Never stop growing. ― Caley Alyssa",
    "If you change nothing, nothing will change. ― Unknown",
    "Dream big dreams. Small dreams have no magic. ― Dottie Boreyko",
    "Don’t go through life, grow through life. ― Eric Butterworth"
)

private val quoteKey = stringPreferencesKey("currentQuote")

class RefreshQuoteAction : ActionCallback {

    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        updateAppWidgetState(context, PreferencesGlanceStateDefinition, glanceId) { preferences ->
            preferences.toMutablePreferences().apply {
                this[quoteKey] = staticQuotes.random()
            }
        }
        ZenQuotesWidget().update(context, glanceId)
    }
}