package id.mzennis.widget

import androidx.glance.appwidget.ExperimentalGlanceRemoteViewsApi
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import com.google.android.glance.tools.viewer.GlanceSnapshot
import com.google.android.glance.tools.viewer.GlanceViewerActivity

@OptIn(ExperimentalGlanceRemoteViewsApi::class)
class MyWidgetViewerActivity : GlanceViewerActivity() {

    override suspend fun getGlanceSnapshot(
        receiver: Class<out GlanceAppWidgetReceiver>
    ): GlanceSnapshot {
        return when (receiver) {
            ZenQuotesWidgetReceiver::class.java -> GlanceSnapshot(
                instance = ZenQuotesWidget()
            )
            else -> throw IllegalArgumentException()
        }
    }

    override fun getProviders() = listOf(ZenQuotesWidgetReceiver::class.java)
}