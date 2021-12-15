package kozyriatskyi.anton.sked.intro

import dagger.Component
import kozyriatskyi.anton.sked.di.AppComponent
import kozyriatskyi.anton.sked.di.Intro
import kozyriatskyi.anton.sked.main.MainComponent

/**
 * Created by Backbase R&D B.V. on 27.11.2021.
 */
@Intro
@Component(dependencies = [AppComponent::class, MainComponent::class])
interface IntroComponent {
    fun inject(fragment: IntroFragment)
}