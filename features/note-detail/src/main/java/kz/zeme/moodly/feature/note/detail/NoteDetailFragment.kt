package kz.zeme.moodly.feature.note.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.livedata.observeAsState
import kz.zeme.moodly.core.domain.RequestCode
import kz.zeme.moodly.core.fragment.BaseFragment
import kz.zeme.moodly.core.fragment.extensions.composeView
import kz.zeme.moodly.feature.note.detail.navigation.NoteDetailNavigator
import kz.zeme.moodly.feature.note.detail.navigation.NoteDetailRouter
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class NoteDetailFragment : BaseFragment() {

    private val noteDetailRouter: NoteDetailRouter by inject()
    private val noteDetailNavigator: NoteDetailNavigator by inject()
    private val noteDetailVM: NoteDetailVM by viewModel {
        parametersOf(noteId, requestCode)
    }

    private val noteId: Long
        get() = noteDetailRouter.getNoteIdOrNull(arguments)!!

    private val requestCode: RequestCode
        get() = noteDetailRouter.getRequestCodeOrNull(arguments) ?: RequestCode.RC_NOTE_DETAIL

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = composeView {
        NoteDetailScreen(
            state = noteDetailVM.state.observeAsState(),
            controller = noteDetailVM,
        )
    }.also {
        noteDetailVM.onStart()
    }

    override fun startObserving() {
        noteDetailVM.navigation.observeEvent(
            owner = viewLifecycleOwner,
            onUpdate = noteDetailNavigator::navigate,
        )
    }
}
