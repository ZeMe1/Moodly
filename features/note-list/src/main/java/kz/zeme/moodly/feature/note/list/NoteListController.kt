package kz.zeme.moodly.feature.note.list

import kz.zeme.moodly.core.ui.components.MoodlyBottomTab

interface NoteListController {

    fun onStart()

    fun onRefresh()

    fun onCreateNoteClick()

    fun onNoteClick(noteId: Long)

    fun onTabClick(tab: MoodlyBottomTab)
}
