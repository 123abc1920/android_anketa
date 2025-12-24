package com.example.task1.di

import com.example.task1.features.settings.domain.SettingsRequests
import org.koin.dsl.module

val runModule = module { single { SettingsRequests() } }