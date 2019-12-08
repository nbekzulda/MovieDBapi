package com.example.moviedbapi.main.DI

import android.util.Log
import com.example.moviedbapi.data.network.MovieApi
import com.example.moviedbapi.data.repositoryIMPL.MovieRepositoryImpl
import com.example.moviedbapi.data.repositoryIMPL.UserRepositoryImpl
import com.example.moviedbapi.data.roomCinema.CinemaRoomDatabase
import com.example.moviedbapi.main.authorization.AuthorizationViewModel
import com.example.moviedbapi.main.cinema.CinemaViewModel
import com.example.moviedbapi.main.details.MovieInfoViewModel
import com.example.moviedbapi.main.favourite.FavoriteMoviesViewModel
import com.example.moviedbapi.main.popularMovie.MoviePopularViewModel
import com.example.moviedbapi.main.user.UserViewModel
import com.example.moviedbapi.repository.MovieRepository
import com.example.moviedbapi.repository.UserRepository
import com.example.moviedbapi.utilities.AppConstants
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {
    single(named("api_key")) { ApiKeyProvider() }
    single(named("base_url")) { BaseUrlProvider() }
    single { HttpLoggingInterceptorProvider() }
    single { StethoInterceptorProvider() }
    single { AuthInterceptorProvider(apiKey = get(named("api_key"))) }
    single { OkHttpProvider(
        loggingInterceptor = get(),
        stethoInterceptor = get(),
        authInterceptor = get()
    )}
    single { CallAdapterFactoryProvider() }
    single { ConverterFactoryProvider() }
    single { RetrofitProvider(
        baseUrl = get(named("base_url")),
        okHttpClient = get(),
        gsonConverterFactory = get(),
        callAdapterFactory = get()
    )}
    single { MovieApiProvider(retrofit = get()) }
}

val repositoryModule = module {
    single { MovieRepositoryProvider(movieApi = get()) }
    single { UserRepositoryProvider(movieApi = get()) }
}

val roomModule = module {
    single { CinemaRoomDatabase.getDatabase(
        context = androidApplication(),
        scope = get()
    ) }

    single(createdAtStart = false) { get<CinemaRoomDatabase>().cinemaDao() }

    factory { SupervisorJob() }
    factory { CoroutineScope(Dispatchers.IO) }
}

val viewModelModule = module {
    viewModel { AuthorizationViewModel(userRepository = get()) }
    viewModel { MoviePopularViewModel(movieRepository = get()) }
    viewModel { MovieInfoViewModel(movieRepository = get()) }
    viewModel { FavoriteMoviesViewModel(movieRepository = get()) }
    viewModel { UserViewModel(userRepository = get()) }
    viewModel { CinemaViewModel(cinemaDao = get()) }
}

val appModule = listOf(networkModule, repositoryModule, viewModelModule, roomModule)



//--------------------------------------Network-----------------------------------------------------



fun HttpLoggingInterceptorProvider(): HttpLoggingInterceptor {
    return HttpLoggingInterceptor(
        HttpLoggingInterceptor.Logger { message -> Log.d("OkHttp", message)}
    ).apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
}

fun ApiKeyProvider(): String = AppConstants.API_KEY

fun BaseUrlProvider(): String = AppConstants.BASE_URL

//--------------------------------------Repository--------------------------------------------------

fun MovieRepositoryProvider(movieApi: MovieApi): MovieRepository = MovieRepositoryImpl(movieApi)

fun UserRepositoryProvider(movieApi: MovieApi): UserRepository = UserRepositoryImpl(movieApi)
fun StethoInterceptorProvider(): StethoInterceptor = StethoInterceptor()



fun OkHttpProvider(
    loggingInterceptor: HttpLoggingInterceptor,
    stethoInterceptor: StethoInterceptor,
    authInterceptor: Interceptor
): OkHttpClient {
    return OkHttpClient.Builder()
        .addNetworkInterceptor(stethoInterceptor)
        .addInterceptor(loggingInterceptor)
        .addInterceptor(authInterceptor)
        .build()
}

fun CallAdapterFactoryProvider(): CallAdapter.Factory = CoroutineCallAdapterFactory()

fun ConverterFactoryProvider(): Converter.Factory = GsonConverterFactory.create()

fun RetrofitProvider(
    baseUrl: String,
    okHttpClient: OkHttpClient,
    gsonConverterFactory: Converter.Factory,
    callAdapterFactory: CallAdapter.Factory
): Retrofit {
    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(gsonConverterFactory)
        .addCallAdapterFactory(callAdapterFactory)
        .build()
}

fun AuthInterceptorProvider(apiKey: String): Interceptor {
    return Interceptor { chain ->
        val newUrl = chain.request().url()
            .newBuilder()
            .addQueryParameter("api_key", apiKey)
            .build()
        val newRequest = chain.request()
            .newBuilder()
            .url(newUrl)
            .build()
        chain.proceed(newRequest)
    }
}
fun MovieApiProvider(retrofit: Retrofit): MovieApi = retrofit.create(MovieApi::class.java)