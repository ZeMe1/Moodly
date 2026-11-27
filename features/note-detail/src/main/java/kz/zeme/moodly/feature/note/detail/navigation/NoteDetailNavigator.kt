package kz.zeme.moodly.feature.note.detail.navigation

import kz.zeme.moodly.core.activity.ActivityProvider
import kz.zeme.moodly.core.fragment.extensions.dismiss
import kz.zeme.moodly.core.fragment.extensions.isShown
import kz.zeme.moodly.core.fragment.extensions.onBackPressed
import kz.zeme.moodly.core.navigation.BaseNavigation
import kz.zeme.moodly.core.navigation.ExternalModulesNavigator
import kz.zeme.moodly.core.navigation.Navigator

class NoteDetailNavigator(
    private val activityProvider: ActivityProvider,
    private val noteDetailRouter: NoteDetailRouter,
    private val externalModulesNavigator: ExternalModulesNavigator,
) : Navigator {

    override fun navigate(navigation: Navigator.Navigation) = when (navigation) {
        is BaseNavigation.GoBack -> goBack()
        is BaseNavigation.Close -> close()
        is NoteDetailNavigation -> navigate(navigation)
        else -> externalModulesNavigator.navigate(navigation)
    }

    private fun navigate(navigation: NoteDetailNavigation) = when (navigation) {
        is NoteDetailNavigation.ShowDeleteConfirmDialog -> showDeleteConfirmDialog(navigation)
    }

    private fun goBack() {
        activityProvider.currentActivity?.supportFragmentManager?.onBackPressed()
    }

    private fun close() {
        activityProvider.currentActivity?.supportFragmentManager?.onBackPressed()
    }

    @Suppress("UNUSED_PARAMETER")
    private fun showDeleteConfirmDialog(navigation: NoteDetailNavigation.ShowDeleteConfirmDialog) {
        val fm = activityProvider.currentActivity?.supportFragmentManager ?: return

        val tag = NoteDetailRouter.TAG_DELETE_NOTE_DIALOG

        if (fm.isShown(tag)) return

        fm.dismiss(tag)

        val instance = noteDetailRouter.getDeleteNoteDialog()
        instance.show(fm, tag)
    }
}
