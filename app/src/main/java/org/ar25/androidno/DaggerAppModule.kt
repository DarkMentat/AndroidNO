package org.ar25.androidno

import android.content.Context
import dagger.Module
import dagger.Provides
import org.ar25.androidno.navigation.ScreenRouterManager
import javax.inject.Singleton


@Module
class DaggerAppModule(

        @ApplicationContext val context: Context
) {

    @Provides
    @ApplicationContext
    internal fun provideContext(): Context {
        return context
    }

    @Provides @Singleton
    fun provideRouterManager(@ApplicationContext context: Context): ScreenRouterManager {
        return ScreenRouterManager(context)
    }
}