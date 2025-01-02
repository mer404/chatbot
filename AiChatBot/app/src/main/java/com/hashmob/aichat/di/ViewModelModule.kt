/*
 * Copyright (C) 2018 The Android Open Source Project
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
package com.hashmob.aichat.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hashmob.aichat.main.home.premium.PremiumViewModel
import com.hashmob.aichat.main.home.tab.chatbot.ChatViewModel
import com.hashmob.aichat.main.home.tab.generateimage.GenerateImageViewModel
import com.hashmob.aichat.main.home.tab.generateimage.generateimageresult.GenerateImageResultViewModel
import com.hashmob.aichat.main.home.tab.scanmathsolving.ScanMathViewModel
import com.hashmob.aichat.main.home.tab.scanmathsolving.solution.SolutionViewModel
import com.hashmob.aichat.main.home.tab.topic.tab.composition.CompositionViewModel
import com.hashmob.aichat.main.home.tab.topic.tab.composition.chooselanguage.LanguageViewModel
import com.hashmob.aichat.main.home.tab.topic.tab.composition.result.ResultViewModel
import com.hashmob.aichat.main.home.tab.topic.ui.CategoriesChatViewModel
import com.hashmob.aichat.main.home.ui.HomeViewModel
import com.hashmob.aichat.main.home.ui.drawer.PrivacyPolicyViewModel
import com.hashmob.aichat.main.intro.IntroViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Since dagger
 * support multibinding you are free to bind as may ViewModels as you want.
 */
@Suppress("unused")
@Module
abstract class ViewModelModule {
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(IntroViewModel::class)
    abstract fun bindIntroViewModel(viewModel: IntroViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindHomeViewModel(viewModel: HomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(GenerateImageViewModel::class)
    abstract fun bindGenerateImageViewModel(viewModel: GenerateImageViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(GenerateImageResultViewModel::class)
    abstract fun bindGenerateImageResultViewModel(viewModel: GenerateImageResultViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PremiumViewModel::class)
    abstract fun bindPremiumViewModel(viewModel: PremiumViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CategoriesChatViewModel::class)
    abstract fun bindCategoriesChatViewModel(viewModel: CategoriesChatViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CompositionViewModel::class)
    abstract fun bindCompositionViewModel(viewModel: CompositionViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ResultViewModel::class)
    abstract fun bindResultViewModel(viewModel: ResultViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LanguageViewModel::class)
    abstract fun bindLanguageViewModel(viewModel: LanguageViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChatViewModel::class)
    abstract fun bindChatViewModel(viewModel: ChatViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PrivacyPolicyViewModel::class)
    abstract fun bindPrivacyPolicyViewModel(viewModel: PrivacyPolicyViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ScanMathViewModel::class)
    abstract fun bindScanMathViewModel(viewModel: ScanMathViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SolutionViewModel::class)
    abstract fun bindSolutionViewModel(viewModel: SolutionViewModel): ViewModel
}
