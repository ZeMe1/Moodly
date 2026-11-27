package kz.zeme.moodly.core.theme.view.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.livedata.observeAsState
import kz.zeme.moodly.core.fragment.BaseBottomSheetDialog
import kz.zeme.moodly.core.fragment.extensions.composeView
import kz.zeme.moodly.core.navigation.BaseNavigation
import kz.zeme.moodly.core.navigation.ExternalModulesNavigator
import kz.zeme.moodly.core.navigation.Navigator
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ThemePickerFragment : BaseBottomSheetDialog() {

    private val themePickerVM: ThemePickerVM by viewModel()
    private val externalModulesNavigator: ExternalModulesNavigator by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = composeView {
        ThemePickerScreen(
            state = themePickerVM.themes.observeAsState(),
            controller = themePickerVM,
        )
    }.also {
        themePickerVM.onStart()
    }

    override fun startObserving() {
        themePickerVM.navigation.observeEvent(
            owner = viewLifecycleOwner,
            onUpdate = ::navigate,
        )
    }

    private fun navigate(navigation: Navigator.Navigation) = when (navigation) {
        is BaseNavigation.GoBack,
        is BaseNavigation.Close -> dismiss()
        else -> externalModulesNavigator.navigate(navigation)
    }
}
