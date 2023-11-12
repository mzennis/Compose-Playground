package id.mzennis.demo.ktor.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.mzennis.demo.ktor.data.CloudService
import id.mzennis.demo.ktor.data.ktorClient
import id.mzennis.demo.ktor.ui.util.UiResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Created by meyta.taliti on 12/11/23.
 */
class HomeViewModel : ViewModel() {

    /* todo: install dependency injection lib */
    private val cloudService = CloudService(ktorClient)
    private val uiMapper = HomeUiMapper()

    private val _uiState = MutableStateFlow<UiResult<HomeUiState>>(UiResult.Loading)

    val uiState: StateFlow<UiResult<HomeUiState>> = _uiState.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        UiResult.Loading
    )

    init {
        loadArticles()
    }

    private fun loadArticles() {
        viewModelScope.launch {
            try {
                val home = withContext(Dispatchers.IO) {
                    val response = cloudService.getArticles()
                    uiMapper.map(response)
                }
                _uiState.update {
                    UiResult.Success(home)
                }
            } catch (err: Throwable) {
                _uiState.update { UiResult.Fail(err) }
            }
        }
    }
}