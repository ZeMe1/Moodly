package kz.zeme.moodly.feature.note.create

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

class NoteCreateVM(
    private val requestCode: RequestCode,
    private val noteRepository: NoteRepository,
    private val noteDateFormatter: NoteDateFormatter,
    private val navigationResult: NavigationResultLiveData,
) : ViewModel(), NoteCreateController {

    private val _state = MutableLiveData<ViewState<NoteCreateState>>()
    private val _navigation = MutableEventFlow<Navigator.Navigation>(viewModelScope)

    private var _initFormJob: Job? = null
    private var _saveNoteJob: Job? = null

    private var _createdAtMs: Long = System.currentTimeMillis()

    val state: LiveData<ViewState<NoteCreateState>> = _state
    val navigation: EventFlow<Navigator.Navigation> = _navigation.asEventFlow()

    override fun onStart() = onInitForm()

    override fun onRefresh() = onInitForm()

    override fun onCloseClick() {
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

        onSaveNote(currentState)
    }

    private fun onInitForm() {
        _initFormJob?.cancel()
        _initFormJob = initForm()
    }

    private fun initForm() = viewModelScope.launch {
        _state.value = _state.getLoadingState()

        _createdAtMs = System.currentTimeMillis()
        val initialState = NoteCreateState(
            emotions = Emotion.entries.toImmutableList(),
            selectedEmotion = Emotion.Anger,
            dateLabel = noteDateFormatter.formatTodayWithTime(_createdAtMs),
        )

        _state.value = _state.getViewState(Response.Success(initialState))
    }

    private fun onSaveNote(currentState: NoteCreateState) {
        _saveNoteJob?.cancel()
        _saveNoteJob = saveNote(currentState)
    }

    private fun saveNote(currentState: NoteCreateState) = viewModelScope.launch {
        _state.update { copy(isSaving = true) }

        val response = noteRepository.createNote(
            emotion = currentState.selectedEmotion,
            text = currentState.text,
            createdAt = _createdAtMs,
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
