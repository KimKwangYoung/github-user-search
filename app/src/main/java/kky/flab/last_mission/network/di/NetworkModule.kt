package kky.flab.last_mission.network.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kky.flab.last_mission.network.api.GithubSearchApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()

        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        builder.addInterceptor(logging)

        return builder.build()
    }

    @Provides
    @Singleton
    fun providesGithubSearchApi(
        json: Json,
        okHttpClient: OkHttpClient,
    ): GithubSearchApi {
        val baseUrl = "https://api.github.com/"
        val retrofit =
            Retrofit.Builder()
                .baseUrl(baseUrl).client(okHttpClient)
                .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
                .build()

        return retrofit.create(GithubSearchApi::class.java)
    }

    @Provides
    @Singleton
    fun providesJson(): Json = Json { ignoreUnknownKeys = true }
}