/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hashmob.aichat.di.component

import android.app.Application
import com.hashmob.aichat.base.BaseApplication
import com.hashmob.aichat.di.ActivityModuleBuilder
import com.hashmob.aichat.di.FragmentModuleBuilder
import com.hashmob.aichat.di.ViewModelModule
import com.hashmob.aichat.di.module.AppModule
import com.hashmob.aichat.di.module.MainModule
import com.hashmob.aichat.util.Preferences
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

//@Singleton
//@Component(
//        modules = [
//            AndroidInjectionModule::class,
//            AppModule::class,
//            MainModule::class,
//            ActivityModuleBuilder::class
//            , ViewModelModule::class
//        ]
//)
//interface AppComponent {
//    @Component.Builder
//    interface Builder {
//        @BindsInstance
//        fun application(application: Application): Builder
//        fun build(): AppComponent
//    }
//
//    fun inject(app: RUSocialApplication)
//}
@Singleton
@Component(
    modules = [AndroidInjectionModule::class,
        AppModule::class,
        MainModule::class,
        ActivityModuleBuilder::class,
        FragmentModuleBuilder::class,
        ViewModelModule::class]
)
interface AppComponent : AndroidInjector<BaseApplication> {
    fun preferences(): Preferences?

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): AppComponent
    }
}