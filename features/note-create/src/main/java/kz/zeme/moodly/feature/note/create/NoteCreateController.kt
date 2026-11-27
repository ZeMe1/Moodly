package kz.zeme.moodly.feature.note.create

import kz.zeme.moodly.core.note.domain.Emotion

interface NoteCreateController {

    fun onStart()

    fun onRefresh()

    fun onCloseClick()

    fun onEmotionSelect(emotion: Emotion)

    fun onTextChange(text: String)

    fun onSaveClick()
}
