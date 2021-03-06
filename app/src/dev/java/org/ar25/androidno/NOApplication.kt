package org.ar25.androidno

import android.app.Application
import android.os.StrictMode
import com.facebook.stetho.Stetho
import com.squareup.picasso.Picasso
import dagger.Component
import org.ar25.androidno.api.DaggerRetrofitModule
import org.ar25.androidno.db.DaggerDbModule
import org.ar25.androidno.db.LocalStorageImpl
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
    @Component(modules = arrayOf(DaggerAppModule::class, DaggerDbModule::class, DaggerRetrofitModule::class))
    interface NOAppComponent {
        fun inject(presenter: MainPresenter)
        fun inject(presenter: DetailPresenter)
        fun inject(activity: MainActivity)
        fun inject(activity: DetailActivity)
        fun inject(localStorage: LocalStorageImpl)
    }

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG && BuildConfig.FLAVOR == "dev") {

            Stetho.initializeWithDefaults(this)

//            StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder()
//                    .detectAll()
//                    .permitNetwork()
//                    .penaltyLog()
//                    .penaltyDialog()
//                    .build())

            StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build())
        }

        noAppComponent = DaggerNOApplication_NOAppComponent
                            .builder()
                            .daggerAppModule(DaggerAppModule(this))
                            .daggerDbModule(DaggerDbModule(this))
                            .daggerRetrofitModule(DaggerRetrofitModule())
                            .build()
    }
}
