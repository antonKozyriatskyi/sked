package kozyriatskyi.anton.sked.di.module

import android.content.Context
import android.content.SharedPreferences
import android.support.v7.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import kozyriatskyi.anton.sked.data.LessonsDatabase
import kozyriatskyi.anton.sked.data.repository.ScheduleDatabase
import kozyriatskyi.anton.sked.data.repository.UserInfoStorage
import kozyriatskyi.anton.sked.data.repository.UserSettingsStorage
import kozyriatskyi.anton.sked.di.App
import kozyriatskyi.anton.sked.repository.ScheduleStorage
import kozyriatskyi.anton.sked.util.ScheduleUpdateTimeLogger
import javax.inject.Named

@Module
class StorageModule {

    companion object {
        const val PREFERENCES_USER_INFO = "user_info"
        const val PREFERENCES_USER_SETTINGS = "user_settings"
    }

    @App
    @Provides
    @Named(PREFERENCES_USER_INFO)
    fun provideInfoSharedPreferences(context: Context): SharedPreferences =
            context.getSharedPreferences(StorageModule.PREFERENCES_USER_INFO, Context.MODE_PRIVATE)

    @App
    @Provides
    @Named(PREFERENCES_USER_SETTINGS)
    fun provideSettingsSharedPreferences(context: Context): SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(context)

    @App
    @Provides
    fun provideUserInfoStorage(@Named(PREFERENCES_USER_INFO) preferences: SharedPreferences): UserInfoStorage =
            UserInfoStorage(preferences)

    @App
    @Provides
    fun providePreferencesStorage(@Named(PREFERENCES_USER_SETTINGS) preferences: SharedPreferences): UserSettingsStorage =
            UserSettingsStorage(preferences)

    @App
    @Provides
    fun provideScheduleStorage(context: Context, scheduleUpdateTimeLogger: ScheduleUpdateTimeLogger): ScheduleStorage {
        val lessonsDatabase = LessonsDatabase.getInstance(context)
        return ScheduleDatabase(lessonsDatabase, scheduleUpdateTimeLogger)
    }

    @Provides
    fun provideScheduleUpdateTimeLogger(@Named(PREFERENCES_USER_SETTINGS)
                                        sharedPreferences: SharedPreferences): ScheduleUpdateTimeLogger {
        return ScheduleUpdateTimeLogger(sharedPreferences)
    }
}