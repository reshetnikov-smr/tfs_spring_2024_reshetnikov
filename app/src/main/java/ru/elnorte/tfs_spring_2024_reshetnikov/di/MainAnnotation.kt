package ru.elnorte.tfs_spring_2024_reshetnikov.di

import javax.inject.Scope

class MainAnnotation {
    @Scope
    @MustBeDocumented
    @Retention(value = AnnotationRetention.RUNTIME)
    annotation class ChatScope

    @Scope
    @MustBeDocumented
    @Retention(value = AnnotationRetention.RUNTIME)
    annotation class ChannelScope

    @Scope
    @MustBeDocumented
    @Retention(value = AnnotationRetention.RUNTIME)
    annotation class ProfileScope

    @Scope
    @MustBeDocumented
    @Retention(value = AnnotationRetention.RUNTIME)
    annotation class ContactScope
}
