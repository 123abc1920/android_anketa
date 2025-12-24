package com.example.task1.di

import com.example.task1.features.editcreate.domain.EditRequests
import org.koin.dsl.module

val editModule = module { single { EditRequests() } }