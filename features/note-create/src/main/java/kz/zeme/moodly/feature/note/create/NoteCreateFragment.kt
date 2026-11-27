package kz.zeme.moodly.feature.note.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.livedata.observeAsState
import kz.zeme.moodly.core.domain.RequestCode
import kz.zeme.moodly.core.fragment.BaseFragment
import kz.zeme.moodly.core.fragment.extensions.composeView
import kz.zeme.moodly.core.navigation.ExternalModulesNavigator
import kz.zeme.moodly.feature.note.create.navigation.NoteCreateRouter
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class NoteCreateFragment : BaseFragment() {

    private val noteCreateRouter: NoteCreateRouter by inject()
    private val externalModulesNavigator: ExternalModulesNavigator by inject()
    private val noteCreateVM: NoteCreateVM by viewModel {
        parametersOf(requestCode)
    }

    private val requestCode: RequestCode
        get() = noteCreateRouter.getRequestCodeOrNull(arguments) ?: RequestCode.RC_NOTE_CREATE

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = composeView {
        NoteCreateScreen(
            state = noteCreateVM.state.observeAsState(),
            controller = noteCreateVM,
        )
    }.also {
        noteCreateVM.onStart()
    }

    override fun startObserving() {
        noteCreateVM.navigation.observeEvent(
            owner = viewLifecycleOwner,
            onUpdate = externalModulesNavigator::navigate,
        )
    }
}
