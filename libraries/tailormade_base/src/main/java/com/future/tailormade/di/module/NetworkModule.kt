package com.future.tailormade.di.module

import android.content.Context
import com.future.tailormade.base.repository.AuthSharedPrefRepository
import com.future.tailormade.di.scope.AuthInterceptor
import com.future.tailormade.di.scope.FirebaseApi
import com.future.tailormade.di.scope.NoAuthInterceptor
import com.future.tailormade.di.scope.OtherApi
import com.future.tailormade.di.scope.TailormadeApi
import com.future.tailormade.di.scope.TailormadeLoginApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber

@Module
@InstallIn(ApplicationComponent::class)
class NetworkModule {

  private val BASE_URL = "https://tailormade-backend.herokuapp.com/"

  private val FIREBASE_URL = "https://tailormade-eac34.firebaseio.com/"

  @Provides
  @Singleton
  fun provideAuthSharedPref(@ApplicationContext context: Context): AuthSharedPrefRepository {
    return AuthSharedPrefRepository.newInstance(context)
  }

  @Provides
  @Singleton
  fun provideAuthInterceptor(authSharedPrefRepository: AuthSharedPrefRepository): Interceptor {
    return Interceptor { chain ->
      val request = chain.request().newBuilder()
          .addHeader("Authorization", "Bearer ${authSharedPrefRepository.accessToken}")
          .build()
      Timber.d(this::class.java.simpleName, request.url())
      chain.proceed(request)
    }
  }

  @Provides
  @AuthInterceptor
  fun provideAuthOkHttpClient(interceptor: Interceptor): OkHttpClient {
    return OkHttpClient.Builder().addInterceptor(interceptor).build()
  }

  @Provides
  @NoAuthInterceptor
  fun provideNoAuthOkHttpClient(): OkHttpClient {
    return OkHttpClient.Builder().build()
  }

  @Provides
  @TailormadeApi
  fun provideBaseRetrofit(@AuthInterceptor okHttpClient: OkHttpClient): Retrofit {
    return buildRetrofit(BASE_URL, okHttpClient)
  }

  @Provides
  @TailormadeLoginApi
  fun provideBaseLoginRetrofit(@NoAuthInterceptor okHttpClient: OkHttpClient): Retrofit {
    return buildRetrofit(BASE_URL, okHttpClient)
  }

  @Provides
  @FirebaseApi
  fun provideFirebaseRetrofit(@AuthInterceptor okHttpClient: OkHttpClient): Retrofit {
    return buildRetrofit(FIREBASE_URL, okHttpClient)
  }

  @Provides
  @OtherApi
  fun provideOtherRetrofitBuilder(@NoAuthInterceptor okHttpClient: OkHttpClient): Retrofit.Builder {
    return Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).client(
        okHttpClient)
  }

  private fun buildRetrofit(url: String, okHttpClient: OkHttpClient) =
      Retrofit.Builder().baseUrl(url).addConverterFactory(
          GsonConverterFactory.create()).client(okHttpClient).build()
}