package kozyriatskyi.anton.sked.di

import javax.inject.Scope

/**
 * Created by Anton on 03.02.2018.
 */
@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class App

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class MainScreen

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class ByDay

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class ByWeek

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class Day

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class Week

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class Settings

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class Updater

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class Login

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class Audiences