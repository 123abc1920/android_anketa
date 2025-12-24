package com.example.task1.di

import com.example.task1.features.user.domain.UserNavigation
import com.example.task1.features.user.domain.UserRequests
import org.koin.dsl.module

val userModule = module {
    single { UserRequests() }
    single { UserNavigation() }
}