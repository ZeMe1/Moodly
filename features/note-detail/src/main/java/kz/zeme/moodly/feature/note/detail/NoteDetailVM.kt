package kz.zeme.moodly.feature.note.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kz.zeme.moodly.core.domain.RequestCode
import kz.zeme.moodly.core.domain.RequestCode.RC_NOTE_DELETE_CONFIRM
import kz.zeme.moodly.core.domain.RequestCode.RC_NOTE_EDIT
import kz.zeme.moodly.core.domain.Response
import kz.zeme.moodly.core.navigation.BaseNavigation
import kz.zeme.moodly.core.navigation.NavigationResultLiveData
import kz.zeme.moodly.core.navigation.NavigationResultLiveData.NavigationResult
import kz.zeme.moodly.core.navigation.NavigationResultLiveData.NavigationResult.DialogResult
import kz.zeme.moodly.core.navigation.Navigator
import kz.zeme.moodly.core.note.domain.Note
import kz.zeme.moodly.core.note.domain.NoteRepository
import kz.zeme.moodly.core.observable.Event
import kz.zeme.moodly.core.observable.EventFlow
import kz.zeme.moodly.core.observable.MutableEventFlow
import kz.zeme.moodly.core.observable.ViewState
import kz.zeme.moodly.core.observable.extensions.getLoadingState
import kz.zeme.moodly.core.observable.extensions.getViewState
import kz.zeme.moodly.feature.note.detail.navigation.NoteDetailExternalNavigation
import kz.zeme.moodly.feature.note.detail.navigation.NoteDetailNavigation

class NoteDetailVM(
    private val noteId: Long,
    private val requestCode: RequestCode,
    private val noteRepository: NoteRepository,
    private val navigationResult: NavigationResultLiveData,
) : ViewModel(), NoteDetailController {

    private val _state = MutableLiveData<ViewState<Note>>()
    private val _navigation = MutableEventFlow<Navigator.Navigation>(viewModelScope)

    private var _loadNoteJob: Job? = null
    private var _deleteNoteJob: Job? = null

    val state: LiveData<ViewState<Note>> = _state
    val navigation: EventFlow<Navigator.Navigation> = _navigation.asEventFlow()

    init {
        _navigation.addSource(navigationResult, ::onNavigationResult)
    }

    override fun onStart() = onLoadNote()

    override fun onRefresh() = onLoadNote()

    override fun onBackClick() {
        _navigation.value = BaseNavigation.GoBack
    }

    override fun onEditClick() {
        _navigation.value = NoteDetailExternalNavigation.ShowNoteEdit(
            noteId = noteId,
            requestCode = RC_NOTE_EDIT,
        )
    }

    override fun onDeleteClick() {
        _navigation.value = NoteDetailNavigation.ShowDeleteConfirmDialog
    }

    private fun onLoadNote() {
        _loadNoteJob?.cancel()
        _loadNoteJob = loadNote()
    }

    private fun loadNote() = viewModelScope.launch {
        _state.value = _state.getLoadingState()

        val response = noteRepository.getNoteById(noteId)

        _state.value = _state.getViewState(response)
    }

    private fun onDeleteNote() {
        _deleteNoteJob?.cancel()
        _deleteNoteJob = deleteNote()
    }

    private fun deleteNote() = viewModelScope.launch {
        when (val response = noteRepository.deleteNote(noteId)) {
            is Response.Success -> {
                navigationResult.setResult(DialogResult.Ok(requestCode = requestCode))
                _navigation.value = BaseNavigation.GoBack
            }

            is Response.Failure -> _navigation.value = BaseNavigation.ShowToast.MessageErr(response.data)
        }
    }

    private fun onNavigationResult(event: Event<NavigationResult>) {
        val result = event.peekContent()
        if (result !is DialogResult.Ok) return

        when (result.requestCode) {
            RC_NOTE_EDIT -> {
                event.getContentIfNotHandled() ?: return
                navigationResult.setResult(DialogResult.Ok(requestCode = requestCode))
                onLoadNote()
            }

            RC_NOTE_DELETE_CONFIRM -> {
                event.getContentIfNotHandled() ?: return
                onDeleteNote()
            }

            else -> Unit
        }
    }
}
