package org.ar25.androidno

import android.app.Application
import dagger.Component
import org.ar25.androidno.api.DaggerRetrofitModule
import org.ar25.androidno.db.DaggerDbModule
import org.ar25.androidno.db.LocalStorageImpl
import org.ar25.androidno.presenters.DaggerPresenterModule
import org.ar25.androidno.presenters.DetailPresenter
import org.ar25.androidno.presenters.MainPresenter
import org.ar25.androidno.ui.DetailActivity
import org.ar25.androidno.ui.MainActivity
import javax.inject.Singleton


class NOApplication : Application() {

    companion object {
        lateinit var noAppComponent: NOAppComponent
    }

    @Singleton
    @Component(modules = arrayOf(DaggerDbModule::class, DaggerPresenterModule::class, DaggerRetrofitModule::class))
    interface NOAppComponent {
        fun inject(presenter: MainPresenter)
        fun inject(presenter: DetailPresenter)
        fun inject(activity: MainActivity)
        fun inject(activity: DetailActivity)
        fun inject(localStorage: LocalStorageImpl)
    }

    override fun onCreate() {
        super.onCreate()

        noAppComponent = DaggerNOApplication_NOAppComponent
                            .builder()
                            .daggerDbModule(DaggerDbModule(this))
                            .daggerPresenterModule(DaggerPresenterModule())
                            .daggerRetrofitModule(DaggerRetrofitModule())
                            .build()
    }
}
