package kz.zeme.moodly.feature.note.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.livedata.observeAsState
import kz.zeme.moodly.core.fragment.BaseFragment
import kz.zeme.moodly.core.fragment.extensions.composeView
import kz.zeme.moodly.core.navigation.ExternalModulesNavigator
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class NoteListFragment : BaseFragment() {

    private val externalModulesNavigator: ExternalModulesNavigator by inject()
    private val noteListVM: NoteListVM by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = composeView {
        NoteListScreen(
            state = noteListVM.state.observeAsState(),
            controller = noteListVM,
        )
    }.also {
        noteListVM.onStart()
    }

    override fun startObserving() {
        noteListVM.navigation.observeEvent(
            owner = viewLifecycleOwner,
            onUpdate = externalModulesNavigator::navigate,
        )
    }
}
