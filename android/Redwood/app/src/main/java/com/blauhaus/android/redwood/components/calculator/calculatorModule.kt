package com.blauhaus.android.redwood.components.calculator

import com.blauhaus.android.redwood.BuildConfig
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val calculatorModule = module {
    single<CalculatorRepo> { CalculatorRepoImpl(get()) }
    factory { CalculatorViewModel(get())}
    factory { CalculatorFragment()}
    factory { CalculatorAuthInterceptor() }
    factory { provideOkHttpClient(get()) }
    single { provideRetrofit(get()) }
    factory { provideCalculatorApi(get())}
}

fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit
        .Builder()
        .baseUrl(BuildConfig.CALCULATOR_API_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create()).build()
}

fun provideOkHttpClient(authInterceptor: CalculatorAuthInterceptor): OkHttpClient {
    return OkHttpClient().newBuilder().addInterceptor(authInterceptor).build()
}

fun provideCalculatorApi(retrofit: Retrofit): CalculatorApi = retrofit.create(CalculatorApi::class.java)

