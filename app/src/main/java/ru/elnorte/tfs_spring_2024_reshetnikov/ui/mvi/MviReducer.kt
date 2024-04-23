package ru.elnorte.tfs_spring_2024_reshetnikov.ui.mvi

interface MviReducer<PartialState : MviPartialState, State : MviState> {
    fun reduce(prevState: State, partialState: PartialState): State
}
