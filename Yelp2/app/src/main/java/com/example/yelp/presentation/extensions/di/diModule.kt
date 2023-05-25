package com.example.yelp.presentation.extensions.di

import com.example.yelp.data.remoteSource.YelpApi
import com.example.yelp.data.remoteSource.createOkHttpClient
import com.example.yelp.data.remoteSource.createWebService
import com.example.yelp.data.repositories.YelpRepository
import com.example.yelp.domain.useCases.*
import com.example.yelp.presentation.base.navigation.BaseScreenFactory
import com.example.yelp.presentation.features.ScreenFactory
import com.example.yelp.presentation.features.businessInformation.BusinessInformationViewModel
import com.example.yelp.presentation.features.businessInformation.businessDetails.BusinessDetailsViewModel
import com.example.yelp.presentation.features.businessInformation.mapLocation.MapLocationViewModel
import com.example.yelp.presentation.features.businessInformation.reviews.ReviewsViewModel
import com.example.yelp.presentation.features.home.HomeViewModel
import com.example.yelp.presentation.features.reservations.ReservationViewModel
import com.example.yelp.presentation.features.spash.SplashViewModel
import com.example.yelp.presentation.viewModels.MiActivityViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

@ExperimentalCoroutinesApi
val diModule = module {

    single<BaseScreenFactory> { ScreenFactory() }

    factory { createOkHttpClient() }

    factory {
        createWebService<YelpApi>(
            okHttpClient = get(),
            url = "https://flash-ocean-368305.wl.r.appspot.com/"
        )
    }

    single<YelpService> { YelpRepository(get(), get()) }

    factory { GetAutoCompleteUseCase(get()) }

    factory { GetSearchResultsUseCase(get()) }

    factory { GetGeoCodingUseCase(get()) }

    factory { GetBusinessDetailsUseCase(get()) }

    factory { GetReviewDataUseCase(get()) }

    factory { SaveReservationsUseCase(get()) }

    factory { GetMapLocationDetailsUseCase(get()) }

    factory { FetchReservationsUseCase(get()) }

    factory { RemoveReservationUseCase(get()) }

    viewModel {
        HomeViewModel(get(), get(), get())
    }

    viewModel {
        SplashViewModel()
    }

    viewModel {
        MiActivityViewModel()
    }

    viewModel {
        BusinessInformationViewModel()
    }

    viewModel {
        BusinessDetailsViewModel(get(), get())
    }

    viewModel {
        MapLocationViewModel(get())
    }

    viewModel {
        ReviewsViewModel(get())
    }

    viewModel {
        ReservationViewModel(get(), get())
    }

    single { provideYelpDatabase(appContext = get()) }
    single { provideReservationRoomDao(database = get()) }

}
