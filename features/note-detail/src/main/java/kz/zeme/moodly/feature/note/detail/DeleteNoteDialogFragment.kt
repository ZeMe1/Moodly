package kz.zeme.moodly.feature.note.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kz.zeme.moodly.core.domain.RequestCode
import kz.zeme.moodly.core.fragment.BaseDialogFragment
import kz.zeme.moodly.core.fragment.extensions.composeView
import kz.zeme.moodly.core.navigation.NavigationResultLiveData
import kz.zeme.moodly.core.navigation.NavigationResultLiveData.NavigationResult.DialogResult
import org.koin.android.ext.android.inject

class DeleteNoteDialogFragment : BaseDialogFragment() {

    private val navigationResult: NavigationResultLiveData by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = composeView {
        DeleteNoteDialogScreen(
            onCancelClick = ::onCancelClick,
            onConfirmClick = ::onConfirmClick,
        )
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
        )
    }

    private fun onCancelClick() {
        navigationResult.setResult(
            DialogResult.Canceled(requestCode = RequestCode.RC_NOTE_DELETE_CONFIRM),
        )
        dismiss()
    }

    private fun onConfirmClick() {
        navigationResult.setResult(
            DialogResult.Ok(requestCode = RequestCode.RC_NOTE_DELETE_CONFIRM),
        )
        dismiss()
    }
}
