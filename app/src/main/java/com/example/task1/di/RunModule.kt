package com.example.task1.di

import com.example.task1.features.runquiz.domain.RunRequests
import org.koin.dsl.module

val runModule = module { single { RunRequests() } }