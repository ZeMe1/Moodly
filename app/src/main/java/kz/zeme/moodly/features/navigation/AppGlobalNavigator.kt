package kz.zeme.moodly.features.navigation

import android.content.Intent
import androidx.core.net.toUri
import androidx.fragment.app.commit
import kz.zeme.moodly.MainActivity
import kz.zeme.moodly.R
import kz.zeme.moodly.core.activity.ActivityProvider
import kz.zeme.moodly.core.fragment.extensions.isShown
import kz.zeme.moodly.core.fragment.extensions.makeToast
import kz.zeme.moodly.core.fragment.extensions.onBackPressed
import kz.zeme.moodly.core.locale.view.presentation.navigation.LanguagePickerNavigation
import kz.zeme.moodly.core.locale.view.presentation.navigation.LanguagePickerRouter
import kz.zeme.moodly.core.navigation.BaseNavigation
import kz.zeme.moodly.core.navigation.ExternalModulesNavigator
import kz.zeme.moodly.core.navigation.Navigator
import kz.zeme.moodly.core.navigation.extensions.putRedirect
import kz.zeme.moodly.core.navigation.extensions.setSlideHorizontalAnimations
import kz.zeme.moodly.core.theme.view.presentation.navigation.ThemePickerNavigation
import kz.zeme.moodly.core.theme.view.presentation.navigation.ThemePickerRouter
import kz.zeme.moodly.feature.emotions.list.navigation.EmotionsListExternalNavigation
import kz.zeme.moodly.feature.emotions.list.navigation.EmotionsListRouter
import kz.zeme.moodly.feature.emotions.navigation.EmotionsExternalNavigation
import kz.zeme.moodly.feature.emotions.navigation.EmotionsRouter
import kz.zeme.moodly.feature.note.create.navigation.NoteCreateExternalNavigation
import kz.zeme.moodly.feature.note.create.navigation.NoteCreateRouter
import kz.zeme.moodly.feature.note.detail.navigation.NoteDetailExternalNavigation
import kz.zeme.moodly.feature.note.detail.navigation.NoteDetailRouter
import kz.zeme.moodly.feature.note.edit.navigation.NoteEditExternalNavigation
import kz.zeme.moodly.feature.note.edit.navigation.NoteEditRouter
import kz.zeme.moodly.feature.note.list.navigation.NoteListExternalNavigation
import kz.zeme.moodly.feature.note.list.navigation.NoteListRouter
import kz.zeme.moodly.feature.settings.navigation.SettingsExternalNavigation
import kz.zeme.moodly.feature.settings.navigation.SettingsRouter
import kz.zeme.moodly.splash.SplashExternalNavigation
import kz.zeme.moodly.splash.SplashFragment

class AppGlobalNavigator(
    private val activityProvider: ActivityProvider,
    private val languagePickerRouter: LanguagePickerRouter,
    private val themePickerRouter: ThemePickerRouter,
    private val noteListRouter: NoteListRouter,
    private val noteCreateRouter: NoteCreateRouter,
    private val noteDetailRouter: NoteDetailRouter,
    private val noteEditRouter: NoteEditRouter,
    private val emotionsRouter: EmotionsRouter,
    private val emotionsListRouter: EmotionsListRouter,
    private val settingsRouter: SettingsRouter,
) : ExternalModulesNavigator {

    override fun navigate(navigation: Navigator.Navigation) = when (navigation) {
        is BaseNavigation.GoBack -> goBack()
        is BaseNavigation.Close -> close()
        is BaseNavigation.ShowMain -> showMain(navigation)
        is BaseNavigation.ShowToast -> showToast(navigation)
        is BaseNavigation.ShowDeeplink -> showDeeplink(navigation)
        is LanguagePickerNavigation.OpenLanguagePicker -> openLanguagePicker()
        is ThemePickerNavigation.OpenThemePicker -> openThemePicker()
        is SplashExternalNavigation.ShowNextScreen -> showSplashNextScreen(navigation)
        is NoteListExternalNavigation.ShowNoteCreate -> showNoteCreate(navigation)
        is NoteListExternalNavigation.ShowNoteDetail -> showNoteDetail(navigation)
        is NoteListExternalNavigation.ShowEmotionsTab -> showEmotionsTab()
        is NoteListExternalNavigation.ShowSettingsTab -> showSettingsTab()
        is NoteCreateExternalNavigation.ShowNoteCreate -> showNoteCreate(navigation)
        is NoteDetailExternalNavigation.ShowNoteEdit -> showNoteEdit(navigation)
        is NoteEditExternalNavigation.ShowNoteEdit -> showNoteEdit(navigation)
        is EmotionsExternalNavigation.ShowEmotionsList -> showEmotionsList(navigation)
        is EmotionsExternalNavigation.ShowNotesTab -> showNotesTab()
        is EmotionsExternalNavigation.ShowSettingsTab -> showSettingsTab()
        is EmotionsListExternalNavigation.ShowNoteDetail -> showNoteDetail(navigation)
        is SettingsExternalNavigation.ShowNotesTab -> showNotesTab()
        is SettingsExternalNavigation.ShowEmotionsTab -> showEmotionsTab()
        else -> Unit
    }

    private fun goBack() {
        activityProvider.currentActivity?.supportFragmentManager?.onBackPressed()
    }

    private fun close() {
        activityProvider.currentActivity?.supportFragmentManager?.onBackPressed()
    }

    private fun showMain(navigation: BaseNavigation.ShowMain) {
        val activity = activityProvider.currentActivity ?: return

        if (navigation.restart) {
            val intent = Intent(activity, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                putRedirect(navigation.redirect)
            }
            activity.finish()
            activity.startActivity(intent)
            return
        }

        val fm = activity.supportFragmentManager

        if (navigation.withSplash) {
            val hasSplash = fm.fragments
                .filterIsInstance<SplashFragment>()
                .isNotEmpty()

            if (hasSplash) return

            fm.commit {
                setReorderingAllowed(true)
                replace(R.id.fragment_container_view, SplashFragment())
            }
            return
        }

        val tag = NoteListRouter.TAG_NOTE_LIST
        val existingFragment = fm.findFragmentByTag(tag)

        if (existingFragment == null) {
            fm.commit {
                setReorderingAllowed(true)
                replace(
                    R.id.fragment_container_view,
                    noteListRouter.getNoteListFragment(),
                    tag,
                )
            }
        }

        navigation.redirect?.let(::navigate)
    }

    private fun showNotesTab() {
        val fm = activityProvider.currentActivity?.supportFragmentManager ?: return
        val tag = NoteListRouter.TAG_NOTE_LIST

        if (fm.isShown(tag)) return

        fm.commit {
            setReorderingAllowed(true)
            replace(
                R.id.fragment_container_view,
                noteListRouter.getNoteListFragment(),
                tag,
            )
        }
    }

    private fun showEmotionsTab() {
        val fm = activityProvider.currentActivity?.supportFragmentManager ?: return
        val tag = EmotionsRouter.TAG_EMOTIONS

        if (fm.isShown(tag)) return

        fm.commit {
            setReorderingAllowed(true)
            replace(
                R.id.fragment_container_view,
                emotionsRouter.getEmotionsFragment(),
                tag,
            )
        }
    }

    private fun showSettingsTab() {
        val fm = activityProvider.currentActivity?.supportFragmentManager ?: return
        val tag = SettingsRouter.TAG_SETTINGS

        if (fm.isShown(tag)) return

        fm.commit {
            setReorderingAllowed(true)
            replace(
                R.id.fragment_container_view,
                settingsRouter.getSettingsFragment(),
                tag,
            )
        }
    }

    private fun showNoteCreate(navigation: NoteListExternalNavigation.ShowNoteCreate) {
        val fm = activityProvider.currentActivity?.supportFragmentManager ?: return
        val tag = NoteCreateRouter.TAG_NOTE_CREATE

        if (fm.isShown(tag)) return

        fm.commit {
            setSlideHorizontalAnimations()
            replace(
                R.id.fragment_container_view,
                noteCreateRouter.getNoteCreateFragment(
                    requestCode = navigation.requestCode,
                ),
                tag,
            )
            addToBackStack(tag)
        }
    }

    private fun showNoteCreate(navigation: NoteCreateExternalNavigation.ShowNoteCreate) {
        val fm = activityProvider.currentActivity?.supportFragmentManager ?: return
        val tag = NoteCreateRouter.TAG_NOTE_CREATE

        if (fm.isShown(tag)) return

        fm.commit {
            setSlideHorizontalAnimations()
            replace(
                R.id.fragment_container_view,
                noteCreateRouter.getNoteCreateFragment(
                    requestCode = navigation.requestCode,
                ),
                tag,
            )
            addToBackStack(tag)
        }
    }

    private fun showNoteDetail(navigation: NoteListExternalNavigation.ShowNoteDetail) {
        val fm = activityProvider.currentActivity?.supportFragmentManager ?: return
        val tag = NoteDetailRouter.TAG_NOTE_DETAIL

        if (fm.isShown(tag)) return

        fm.commit {
            setSlideHorizontalAnimations()
            replace(
                R.id.fragment_container_view,
                noteDetailRouter.getNoteDetailFragment(
                    noteId = navigation.noteId,
                    requestCode = navigation.requestCode,
                ),
                tag,
            )
            addToBackStack(tag)
        }
    }

    private fun showNoteDetail(navigation: EmotionsListExternalNavigation.ShowNoteDetail) {
        val fm = activityProvider.currentActivity?.supportFragmentManager ?: return
        val tag = NoteDetailRouter.TAG_NOTE_DETAIL

        if (fm.isShown(tag)) return

        fm.commit {
            setSlideHorizontalAnimations()
            replace(
                R.id.fragment_container_view,
                noteDetailRouter.getNoteDetailFragment(
                    noteId = navigation.noteId,
                    requestCode = navigation.requestCode,
                ),
                tag,
            )
            addToBackStack(tag)
        }
    }

    private fun showNoteEdit(navigation: NoteDetailExternalNavigation.ShowNoteEdit) {
        val fm = activityProvider.currentActivity?.supportFragmentManager ?: return
        val tag = NoteEditRouter.TAG_NOTE_EDIT

        if (fm.isShown(tag)) return

        fm.commit {
            setSlideHorizontalAnimations()
            replace(
                R.id.fragment_container_view,
                noteEditRouter.getNoteEditFragment(
                    noteId = navigation.noteId,
                    requestCode = navigation.requestCode,
                ),
                tag,
            )
            addToBackStack(tag)
        }
    }

    private fun showNoteEdit(navigation: NoteEditExternalNavigation.ShowNoteEdit) {
        val fm = activityProvider.currentActivity?.supportFragmentManager ?: return
        val tag = NoteEditRouter.TAG_NOTE_EDIT

        if (fm.isShown(tag)) return

        fm.commit {
            setSlideHorizontalAnimations()
            replace(
                R.id.fragment_container_view,
                noteEditRouter.getNoteEditFragment(
                    noteId = navigation.noteId,
                    requestCode = navigation.requestCode,
                ),
                tag,
            )
            addToBackStack(tag)
        }
    }

    private fun showEmotionsList(navigation: EmotionsExternalNavigation.ShowEmotionsList) {
        val fm = activityProvider.currentActivity?.supportFragmentManager ?: return
        val tag = EmotionsListRouter.TAG_EMOTIONS_LIST

        if (fm.isShown(tag)) return

        fm.commit {
            setSlideHorizontalAnimations()
            replace(
                R.id.fragment_container_view,
                emotionsListRouter.getEmotionsListFragment(
                    emotion = navigation.emotion,
                ),
                tag,
            )
            addToBackStack(tag)
        }
    }

    private fun openLanguagePicker() {
        val fm = activityProvider.currentActivity?.supportFragmentManager ?: return
        val tag = LanguagePickerRouter.TAG_LANGUAGE_PICKER

        if (fm.isShown(tag)) return

        languagePickerRouter
            .getLanguagePickerFragment()
            .show(fm, tag)
    }

    private fun openThemePicker() {
        val fm = activityProvider.currentActivity?.supportFragmentManager ?: return
        val tag = ThemePickerRouter.TAG_THEME_PICKER

        if (fm.isShown(tag)) return

        themePickerRouter
            .getThemePickerFragment()
            .show(fm, tag)
    }

    private fun showSplashNextScreen(navigation: SplashExternalNavigation.ShowNextScreen) {
        val redirect = (navigation.redirect as? BaseNavigation.ShowMain)?.redirect ?: navigation.redirect
        showMain(
            BaseNavigation.ShowMain(
                restart = false,
                redirect = redirect,
                withSplash = false,
            ),
        )
    }

    private fun showToast(navigation: BaseNavigation.ShowToast) {
        activityProvider.currentActivity?.makeToast(navigation)
    }

    private fun showDeeplink(navigation: BaseNavigation.ShowDeeplink) {
        val context = activityProvider.currentActivity ?: return
        runCatching {
            context.startActivity(Intent(Intent.ACTION_VIEW, navigation.url.toUri()))
        }
    }
}
