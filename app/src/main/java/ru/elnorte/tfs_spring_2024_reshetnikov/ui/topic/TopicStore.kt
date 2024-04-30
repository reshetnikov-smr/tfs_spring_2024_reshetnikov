package ru.elnorte.tfs_spring_2024_reshetnikov.ui.topic

import ru.elnorte.tfs_spring_2024_reshetnikov.di.MainAnnotation
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.mvi.MviStore
import javax.inject.Inject

@MainAnnotation.ChatScope
class TopicStore @Inject constructor(reducer: TopicReducer, actor: TopicActor) :
    MviStore<TopicPartialState, TopicIntent, TopicState, TopicEffect>(reducer, actor) {
    override fun initialStateCreator(): TopicState = TopicLoading
}
