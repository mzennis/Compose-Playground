package id.mzennis.rates.util

import android.icu.text.DecimalFormat
import android.icu.text.DecimalFormatSymbols
import java.util.Locale
import javax.inject.Inject

/**
 * Created by meyta.taliti on 29/10/23.
 */
class CurrencyFormatter @Inject constructor() {

    private val formatter = DecimalFormat("#,###.##", DecimalFormatSymbols(Locale.getDefault()))

    fun formatNumber(
        number: Double
    ): String {
        return formatter.format(number)
    }
}