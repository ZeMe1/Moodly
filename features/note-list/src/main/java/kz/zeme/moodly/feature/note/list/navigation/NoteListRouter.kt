package kz.zeme.moodly.feature.note.list.navigation

import kz.zeme.moodly.feature.note.list.NoteListFragment

class NoteListRouter {

    companion object {
        const val TAG_NOTE_LIST = "TAG_NOTE_LIST"
    }

    fun getNoteListFragment(): NoteListFragment = NoteListFragment()
}
