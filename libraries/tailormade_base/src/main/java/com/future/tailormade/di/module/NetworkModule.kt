package com.future.tailormade.di.module

import android.util.Log
import com.future.tailormade.di.scope.FirebaseApi
import com.future.tailormade.di.scope.OtherApi
import com.future.tailormade.di.scope.TailormadeApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class NetworkModule {

  // TODO : Change base url to actual api url
  private val BASE_URL = "https://tailormade-api.com"

  private val FIREBASE_URL = "https://tailormade-eac34.firebaseio.com"

  @Provides
  @TailormadeApi
  fun provideBaseUrl() = BASE_URL

  @Provides
  @FirebaseApi
  fun provideFirebaseUrl() = FIREBASE_URL

  @Provides
  @Singleton
  fun provideAuthInterceptor(): Interceptor {
    return Interceptor { chain ->
      val request = chain.request().newBuilder().build()
      Timber.d(this::class.java.simpleName, request.url())
      chain.proceed(request)
    }
  }

  @Provides
  @Singleton
  fun provideOkHttpClient(interceptor: Interceptor): OkHttpClient {
    return OkHttpClient.Builder().addInterceptor(interceptor).build()
  }

  @Provides
  @TailormadeApi
  fun provideBaseRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return buildRetrofit(BASE_URL, okHttpClient)
  }

  @Provides
  @FirebaseApi
  fun provideFirebaseRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return buildRetrofit(FIREBASE_URL, okHttpClient)
  }

  @Provides
  @OtherApi
  fun provideOtherRetrofitBuilder(okHttpClient: OkHttpClient): Retrofit.Builder {
    return Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).client(
        okHttpClient)
  }

  private fun buildRetrofit(url: String, okHttpClient: OkHttpClient) =
      Retrofit.Builder().baseUrl(url).addConverterFactory(
          GsonConverterFactory.create()).client(okHttpClient).build()
}