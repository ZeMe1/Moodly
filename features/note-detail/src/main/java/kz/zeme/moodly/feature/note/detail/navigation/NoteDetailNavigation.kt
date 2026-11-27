package kz.zeme.moodly.feature.note.detail.navigation

import kotlinx.parcelize.Parcelize
import kz.zeme.moodly.core.navigation.Navigator

@Parcelize
sealed class NoteDetailNavigation : Navigator.Navigation {

    data object ShowDeleteConfirmDialog : NoteDetailNavigation()
}
