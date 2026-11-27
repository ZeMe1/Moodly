package kz.zeme.moodly.feature.emotions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kz.zeme.moodly.core.domain.Response
import kz.zeme.moodly.core.navigation.Navigator
import kz.zeme.moodly.core.note.domain.Emotion
import kz.zeme.moodly.core.observable.EventFlow
import kz.zeme.moodly.core.observable.MutableEventFlow
import kz.zeme.moodly.core.observable.ViewState
import kz.zeme.moodly.core.observable.extensions.getLoadingState
import kz.zeme.moodly.core.observable.extensions.getViewState
import kz.zeme.moodly.core.ui.components.MoodlyBottomTab
import kz.zeme.moodly.feature.emotions.navigation.EmotionsExternalNavigation

class EmotionsVM : ViewModel(), EmotionsController {

    private val _state = MutableLiveData<ViewState<List<Emotion>>>()
    private val _navigation = MutableEventFlow<Navigator.Navigation>(viewModelScope)

    private var _loadEmotionsJob: Job? = null

    val state: LiveData<ViewState<List<Emotion>>> = _state
    val navigation: EventFlow<Navigator.Navigation> = _navigation.asEventFlow()

    override fun onStart() = onLoadEmotions()

    override fun onRefresh() = onLoadEmotions()

    override fun onEmotionClick(emotion: Emotion) {
        _navigation.value = EmotionsExternalNavigation.ShowEmotionsList(emotion = emotion)
    }

    override fun onTabClick(tab: MoodlyBottomTab) = when (tab) {
        MoodlyBottomTab.Notes -> _navigation.value = EmotionsExternalNavigation.ShowNotesTab
        MoodlyBottomTab.Emotions -> Unit
        MoodlyBottomTab.Settings -> _navigation.value = EmotionsExternalNavigation.ShowSettingsTab
    }

    private fun onLoadEmotions() {
        _loadEmotionsJob?.cancel()
        _loadEmotionsJob = loadEmotions()
    }

    private fun loadEmotions() = viewModelScope.launch {
        _state.value = _state.getLoadingState()

        val response = Response.Success(Emotion.entries.toList())

        _state.value = _state.getViewState(response)
    }
}
