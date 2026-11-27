package kz.zeme.moodly.core.theme.view.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kz.zeme.moodly.core.domain.Response
import kz.zeme.moodly.core.navigation.BaseNavigation.Close
import kz.zeme.moodly.core.navigation.BaseNavigation.ShowMain
import kz.zeme.moodly.core.navigation.BaseNavigation.ShowToast
import kz.zeme.moodly.core.navigation.Navigator
import kz.zeme.moodly.core.observable.EventFlow
import kz.zeme.moodly.core.observable.MutableEventFlow
import kz.zeme.moodly.core.observable.ViewState
import kz.zeme.moodly.core.observable.extensions.getErrorState
import kz.zeme.moodly.core.observable.extensions.getLoadingState
import kz.zeme.moodly.core.observable.extensions.getViewState
import kz.zeme.moodly.core.res.R
import kz.zeme.moodly.core.theme.domain.AppTheme
import kz.zeme.moodly.core.theme.domain.ThemeRepository

open class ThemePickerVM(
    private val themeRepository: ThemeRepository,
) : ViewModel(), ThemePickerController {

    private val _navigation = MutableEventFlow<Navigator.Navigation>(viewModelScope)
    private val _themes = MutableLiveData<ViewState<List<AppTheme>>>()

    private var _getThemesJob: Job? = null
    private var _saveThemeJob: Job? = null

    val navigation: EventFlow<Navigator.Navigation> = _navigation.asEventFlow()
    val themes: LiveData<ViewState<List<AppTheme>>> = _themes

    override fun onThemeClick(theme: AppTheme) = onSaveSelectedTheme(theme)

    override fun onClose() {
        _navigation.value = Close
    }

    override fun onStart() = onLoadThemes()

    private fun onLoadThemes() {
        _getThemesJob?.cancel()
        _getThemesJob = loadThemes()
    }

    private fun loadThemes() = viewModelScope.launch {
        _themes.value = _themes.getLoadingState()

        val response = themeRepository.getThemes()

        _themes.value = _themes.getViewState(response)
    }

    private fun onSaveSelectedTheme(theme: AppTheme) {
        _saveThemeJob?.cancel()
        _saveThemeJob = saveTheme(theme)
    }

    private fun saveTheme(theme: AppTheme) = viewModelScope.launch {
        _themes.value = _themes.getLoadingState()

        when (val response = themeRepository.saveSelectedTheme(theme)) {
            is Response.Failure -> _themes.value = _themes.getErrorState(response)
            is Response.Success -> {
                _navigation.value = ShowToast.MessageRes(R.string.theme_picker_changed_message)
                _navigation.value = ShowMain(
                    restart = true,
                    withSplash = true,
                )
            }
        }
    }
}
