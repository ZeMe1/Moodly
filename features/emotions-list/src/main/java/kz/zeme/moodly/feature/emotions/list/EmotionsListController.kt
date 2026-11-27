package kz.zeme.moodly.feature.emotions.list

interface EmotionsListController {

    fun onStart()

    fun onRefresh()

    fun onBackClick()

    fun onNoteClick(noteId: Long)
}
