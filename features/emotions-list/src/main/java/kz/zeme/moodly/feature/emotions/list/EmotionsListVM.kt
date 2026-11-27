package kz.zeme.moodly.feature.emotions.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kz.zeme.moodly.core.domain.RequestCode.RC_EMOTIONS_LIST_NOTE_DETAIL
import kz.zeme.moodly.core.navigation.BaseNavigation
import kz.zeme.moodly.core.navigation.NavigationResultLiveData
import kz.zeme.moodly.core.navigation.NavigationResultLiveData.NavigationResult
import kz.zeme.moodly.core.navigation.NavigationResultLiveData.NavigationResult.DialogResult
import kz.zeme.moodly.core.navigation.Navigator
import kz.zeme.moodly.core.note.domain.Emotion
import kz.zeme.moodly.core.note.domain.NoteRepository
import kz.zeme.moodly.core.observable.Event
import kz.zeme.moodly.core.observable.EventFlow
import kz.zeme.moodly.core.observable.MutableEventFlow
import kz.zeme.moodly.core.observable.ViewState
import kz.zeme.moodly.core.observable.extensions.getLoadingState
import kz.zeme.moodly.core.observable.extensions.getViewState
import kz.zeme.moodly.feature.emotions.list.navigation.EmotionsListExternalNavigation

class EmotionsListVM(
    private val emotion: Emotion,
    private val noteRepository: NoteRepository,
    navigationResult: NavigationResultLiveData,
) : ViewModel(), EmotionsListController {

    private val _state = MutableLiveData<ViewState<EmotionsListState>>()
    private val _navigation = MutableEventFlow<Navigator.Navigation>(viewModelScope)

    private var _loadNotesJob: Job? = null

    val state: LiveData<ViewState<EmotionsListState>> = _state
    val navigation: EventFlow<Navigator.Navigation> = _navigation.asEventFlow()

    init {
        _navigation.addSource(navigationResult, ::onNavigationResult)
    }

    override fun onStart() = onLoadNotes()

    override fun onRefresh() = onLoadNotes()

    override fun onBackClick() {
        _navigation.value = BaseNavigation.GoBack
    }

    override fun onNoteClick(noteId: Long) {
        _navigation.value = EmotionsListExternalNavigation.ShowNoteDetail(
            noteId = noteId,
            requestCode = RC_EMOTIONS_LIST_NOTE_DETAIL,
        )
    }

    private fun onLoadNotes() {
        _loadNotesJob?.cancel()
        _loadNotesJob = loadNotes()
    }

    private fun loadNotes() = viewModelScope.launch {
        _state.value = _state.getLoadingState()

        val response = noteRepository.getNotesByEmotion(emotion)

        _state.value = _state.getViewState(
            response = response,
            transform = { notes ->
                EmotionsListState(
                    emotion = emotion,
                    notes = notes.toImmutableList(),
                )
            },
        )
    }

    private fun onNavigationResult(event: Event<NavigationResult>) {
        val result = event.peekContent()
        if (result !is DialogResult.Ok) return

        when (result.requestCode) {
            RC_EMOTIONS_LIST_NOTE_DETAIL -> {
                event.getContentIfNotHandled() ?: return
                onRefresh()
            }

            else -> Unit
        }
    }
}
