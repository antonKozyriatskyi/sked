package kozyriatskyi.anton.sked.di.module

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import kozyriatskyi.anton.sked.BuildConfig
import kozyriatskyi.anton.sked.data.LocalDateJsonAdapter
import kozyriatskyi.anton.sked.data.api.ClassroomsApi
import kozyriatskyi.anton.sked.data.api.StudentApi
import kozyriatskyi.anton.sked.data.api.TeacherApi
import kozyriatskyi.anton.sked.di.App
import kozyriatskyi.anton.sked.util.DateFormatter
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

@Module
class NetworkingModule {

    @App
    @Provides
    fun provideMoshi(dateFormatter: DateFormatter): Moshi = Moshi.Builder()
        .add(LocalDateJsonAdapter(dateFormatter))
        .build()

    @App
    @Provides
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .readTimeout(60, TimeUnit.SECONDS)
        .callTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .connectTimeout(60, TimeUnit.SECONDS)
        .apply {
            if (BuildConfig.DEBUG) {
                val interceptor = HttpLoggingInterceptor()
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
                addInterceptor(interceptor)
            }
        }
        .build()

    @App
    @Provides
    fun provideRetrofit(client: OkHttpClient, moshi: Moshi): Retrofit = Retrofit.Builder()
        .baseUrl("https://app-sked.herokuapp.com/v1/")
        .client(client)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    @App
    @Provides
    fun provideStudentApi(retrofit: Retrofit): StudentApi = retrofit.create()

    @App
    @Provides
    fun provideTeacherApi(retrofit: Retrofit): TeacherApi = retrofit.create()

    @App
    @Provides
    fun provideClassroomsApi(retrofit: Retrofit): ClassroomsApi = retrofit.create()
}