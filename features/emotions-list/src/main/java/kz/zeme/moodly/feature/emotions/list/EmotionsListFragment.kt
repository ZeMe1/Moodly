package kz.zeme.moodly.feature.emotions.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.livedata.observeAsState
import kz.zeme.moodly.core.fragment.BaseFragment
import kz.zeme.moodly.core.fragment.extensions.composeView
import kz.zeme.moodly.core.navigation.ExternalModulesNavigator
import kz.zeme.moodly.core.note.domain.Emotion
import kz.zeme.moodly.feature.emotions.list.navigation.EmotionsListRouter
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class EmotionsListFragment : BaseFragment() {

    private val emotionsListRouter: EmotionsListRouter by inject()
    private val externalModulesNavigator: ExternalModulesNavigator by inject()
    private val emotionsListVM: EmotionsListVM by viewModel {
        parametersOf(emotion)
    }

    private val emotion: Emotion
        get() = emotionsListRouter.getEmotionOrNull(arguments)!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = composeView {
        EmotionsListScreen(
            state = emotionsListVM.state.observeAsState(),
            controller = emotionsListVM,
        )
    }.also {
        emotionsListVM.onStart()
    }

    override fun startObserving() {
        emotionsListVM.navigation.observeEvent(
            owner = viewLifecycleOwner,
            onUpdate = externalModulesNavigator::navigate,
        )
    }
}
