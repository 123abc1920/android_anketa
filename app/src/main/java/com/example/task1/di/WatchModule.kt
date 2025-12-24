package com.example.task1.di

import com.example.task1.features.watch.domain.WatchRequests
import org.koin.dsl.module

val watchModule = module {
    single { WatchRequests() }
}
