package org.ar25.androidno.presenters

import org.ar25.androidno.NOApplication

import javax.inject.Singleton

import dagger.Module
import dagger.Provides


@Module
class DaggerPresenterModule {

    @Provides @Singleton
    internal fun provideMainPresenter(): MainPresenter {
        val presenter = MainPresenter()

        NOApplication.noAppComponent.inject(presenter)

        return presenter
    }

    @Provides @Singleton
    internal fun provideDetailPresenter(): DetailPresenter {
        val presenter = DetailPresenter()

        NOApplication.noAppComponent.inject(presenter)

        return presenter
    }
}
