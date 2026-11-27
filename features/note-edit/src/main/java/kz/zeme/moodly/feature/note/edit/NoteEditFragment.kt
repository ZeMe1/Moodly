package kz.zeme.moodly.feature.note.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.livedata.observeAsState
import kz.zeme.moodly.core.domain.RequestCode
import kz.zeme.moodly.core.fragment.BaseFragment
import kz.zeme.moodly.core.fragment.extensions.composeView
import kz.zeme.moodly.core.navigation.ExternalModulesNavigator
import kz.zeme.moodly.feature.note.edit.navigation.NoteEditRouter
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class NoteEditFragment : BaseFragment() {

    private val noteEditRouter: NoteEditRouter by inject()
    private val externalModulesNavigator: ExternalModulesNavigator by inject()
    private val noteEditVM: NoteEditVM by viewModel {
        parametersOf(noteId, requestCode)
    }

    private val noteId: Long
        get() = noteEditRouter.getNoteIdOrNull(arguments)!!

    private val requestCode: RequestCode
        get() = noteEditRouter.getRequestCodeOrNull(arguments) ?: RequestCode.RC_NOTE_EDIT

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = composeView {
        NoteEditScreen(
            state = noteEditVM.state.observeAsState(),
            controller = noteEditVM,
        )
    }.also {
        noteEditVM.onStart()
    }

    override fun startObserving() {
        noteEditVM.navigation.observeEvent(
            owner = viewLifecycleOwner,
            onUpdate = externalModulesNavigator::navigate,
        )
    }
}
