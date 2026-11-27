package kz.zeme.moodly.feature.note.edit

import kz.zeme.moodly.core.note.domain.Emotion

interface NoteEditController {

    fun onStart()

    fun onRefresh()

    fun onBackClick()

    fun onEmotionSelect(emotion: Emotion)

    fun onTextChange(text: String)

    fun onSaveClick()
}
