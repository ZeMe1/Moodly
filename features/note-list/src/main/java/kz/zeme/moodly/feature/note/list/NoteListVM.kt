package kz.zeme.moodly.feature.note.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kz.zeme.moodly.core.domain.RequestCode.RC_NOTE_CREATE
import kz.zeme.moodly.core.domain.RequestCode.RC_NOTE_DETAIL
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
import kz.zeme.moodly.core.ui.components.MoodlyBottomTab
import kz.zeme.moodly.core.ui.formatter.NoteDateFormatter
import kz.zeme.moodly.feature.note.list.navigation.NoteListExternalNavigation

class NoteListVM(
    private val noteRepository: NoteRepository,
    private val noteDateFormatter: NoteDateFormatter,
    navigationResult: NavigationResultLiveData,
) : ViewModel(), NoteListController {

    private val _state = MutableLiveData<ViewState<NoteListState>>()
    private val _navigation = MutableEventFlow<Navigator.Navigation>(viewModelScope)

    private var _loadNotesJob: Job? = null

    val state: LiveData<ViewState<NoteListState>> = _state
    val navigation: EventFlow<Navigator.Navigation> = _navigation.asEventFlow()

    init {
        _navigation.addSource(navigationResult, ::onNavigationResult)
    }

    override fun onStart() = onLoadNotes()

    override fun onRefresh() = onLoadNotes()

    override fun onCreateNoteClick() {
        _navigation.value = NoteListExternalNavigation.ShowNoteCreate()
    }

    override fun onNoteClick(noteId: Long) {
        _navigation.value = NoteListExternalNavigation.ShowNoteDetail(noteId = noteId)
    }

    override fun onTabClick(tab: MoodlyBottomTab) = when (tab) {
        MoodlyBottomTab.Notes -> Unit
        MoodlyBottomTab.Emotions -> _navigation.value = NoteListExternalNavigation.ShowEmotionsTab
        MoodlyBottomTab.Settings -> _navigation.value = NoteListExternalNavigation.ShowSettingsTab
    }

    private fun onLoadNotes() {
        _loadNotesJob?.cancel()
        _loadNotesJob = loadNotes()
    }

    private fun loadNotes() = viewModelScope.launch {
        _state.value = _state.getLoadingState()

        val response = noteRepository.getAllNotes()

        _state.value = _state.getViewState(
            response = response,
            transform = ::mapToState,
        )
    }

    private fun mapToState(notes: List<Note>): NoteListState {
        val grouped = LinkedHashMap<Long, MutableList<NoteCardItem>>()

        notes.forEach { note ->
            val epochDay = noteDateFormatter.toEpochDay(note.createdAt)
            val card = NoteCardItem(
                id = note.id,
                emotion = note.emotion,
                timeText = noteDateFormatter.formatTime(note.createdAt),
                text = note.text,
            )
            grouped.getOrPut(epochDay) { mutableListOf() }.add(card)
        }

        val dayGroups = grouped.entries.map { entry ->
            val firstTimestamp = notes.first {
                noteDateFormatter.toEpochDay(it.createdAt) == entry.key
            }.createdAt
            NoteDayGroup(
                dayTitle = noteDateFormatter.formatDayHeader(firstTimestamp),
                notes = entry.value.toImmutableList(),
            )
        }.toImmutableList()

        return NoteListState(dayGroups = dayGroups)
    }

    private fun onNavigationResult(event: Event<NavigationResult>) {
        val result = event.peekContent()
        if (result !is DialogResult.Ok) return

        when (result.requestCode) {
            RC_NOTE_CREATE,
            RC_NOTE_DETAIL -> {
                event.getContentIfNotHandled() ?: return
                onRefresh()
            }

            else -> Unit
        }
    }
}
