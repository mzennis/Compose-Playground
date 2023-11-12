package id.mzennis.demo.ktor.ui.util

/**
 * Created by meyta.taliti on 31/10/23.
 */
sealed interface UiResult<out T> {
    object Loading : UiResult<Nothing>
    data class Success<T>(val data: T) : UiResult<T>
    data class Fail(val error: Throwable) : UiResult<Nothing>
}