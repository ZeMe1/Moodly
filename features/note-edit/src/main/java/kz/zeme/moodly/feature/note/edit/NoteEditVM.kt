package kz.zeme.moodly.feature.note.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kz.zeme.moodly.core.domain.RequestCode
import kz.zeme.moodly.core.domain.Response
import kz.zeme.moodly.core.navigation.BaseNavigation
import kz.zeme.moodly.core.navigation.NavigationResultLiveData
import kz.zeme.moodly.core.navigation.NavigationResultLiveData.NavigationResult.DialogResult
import kz.zeme.moodly.core.navigation.Navigator
import kz.zeme.moodly.core.note.domain.Emotion
import kz.zeme.moodly.core.note.domain.NoteRepository
import kz.zeme.moodly.core.observable.EventFlow
import kz.zeme.moodly.core.observable.MutableEventFlow
import kz.zeme.moodly.core.observable.ViewState
import kz.zeme.moodly.core.observable.extensions.getLoadingState
import kz.zeme.moodly.core.observable.extensions.getViewState
import kz.zeme.moodly.core.observable.extensions.update
import kz.zeme.moodly.core.ui.formatter.NoteDateFormatter

class NoteEditVM(
    private val noteId: Long,
    private val requestCode: RequestCode,
    private val noteRepository: NoteRepository,
    private val noteDateFormatter: NoteDateFormatter,
    private val navigationResult: NavigationResultLiveData,
) : ViewModel(), NoteEditController {

    private val _state = MutableLiveData<ViewState<NoteEditState>>()
    private val _navigation = MutableEventFlow<Navigator.Navigation>(viewModelScope)

    private var _loadNoteJob: Job? = null
    private var _updateNoteJob: Job? = null

    val state: LiveData<ViewState<NoteEditState>> = _state
    val navigation: EventFlow<Navigator.Navigation> = _navigation.asEventFlow()

    override fun onStart() = onLoadNote()

    override fun onRefresh() = onLoadNote()

    override fun onBackClick() {
        _navigation.value = BaseNavigation.GoBack
    }

    override fun onEmotionSelect(emotion: Emotion) {
        _state.update { copy(selectedEmotion = emotion) }
    }

    override fun onTextChange(text: String) {
        _state.update {
            copy(
                text = text,
                canSave = text.isNotBlank(),
            )
        }
    }

    override fun onSaveClick() {
        val currentState = _state.value?.data ?: return
        if (!currentState.canSave || currentState.isSaving) return

        onUpdateNote(currentState)
    }

    private fun onLoadNote() {
        _loadNoteJob?.cancel()
        _loadNoteJob = loadNote()
    }

    private fun loadNote() = viewModelScope.launch {
        _state.value = _state.getLoadingState()

        val response = noteRepository.getNoteById(noteId)

        _state.value = _state.getViewState(
            response = response,
            transform = { note ->
                NoteEditState(
                    emotions = Emotion.entries.toImmutableList(),
                    selectedEmotion = note.emotion,
                    text = note.text,
                    createdAtLabel = noteDateFormatter.formatCreatedAt(note.createdAt),
                    canSave = note.text.isNotBlank(),
                )
            },
        )
    }

    private fun onUpdateNote(currentState: NoteEditState) {
        _updateNoteJob?.cancel()
        _updateNoteJob = updateNote(currentState)
    }

    private fun updateNote(currentState: NoteEditState) = viewModelScope.launch {
        _state.update { copy(isSaving = true) }

        val response = noteRepository.updateNote(
            id = noteId,
            emotion = currentState.selectedEmotion,
            text = currentState.text,
        )

        when (response) {
            is Response.Success -> {
                navigationResult.setResult(DialogResult.Ok(requestCode = requestCode))
                _navigation.value = BaseNavigation.GoBack
            }

            is Response.Failure -> {
                _state.update { copy(isSaving = false) }
                _navigation.value = BaseNavigation.ShowToast.MessageErr(response.data)
            }
        }
    }
}
