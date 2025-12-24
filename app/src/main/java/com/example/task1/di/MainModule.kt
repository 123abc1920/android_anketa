package com.example.task1.di

import com.example.task1.features.mainpage.domain.MainNavigate
import com.example.task1.features.mainpage.domain.MainRequests
import org.koin.dsl.module

val mainModule = module {
    single { MainRequests() }
    single { MainNavigate() }
}