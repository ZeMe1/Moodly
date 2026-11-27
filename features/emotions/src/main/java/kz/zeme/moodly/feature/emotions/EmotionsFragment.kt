package kz.zeme.moodly.feature.emotions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kz.zeme.moodly.core.fragment.BaseFragment
import kz.zeme.moodly.core.fragment.extensions.composeView
import kz.zeme.moodly.core.navigation.ExternalModulesNavigator
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class EmotionsFragment : BaseFragment() {

    private val externalModulesNavigator: ExternalModulesNavigator by inject()
    private val emotionsVM: EmotionsVM by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = composeView {
        EmotionsScreen(
            controller = emotionsVM,
        )
    }

    override fun startObserving() {
        emotionsVM.navigation.observeEvent(
            owner = viewLifecycleOwner,
            onUpdate = externalModulesNavigator::navigate,
        )
    }
}
