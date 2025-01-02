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
package com.hashmob.aichat.di

import com.hashmob.aichat.di.scope.MainScope
import com.hashmob.aichat.main.home.premium.PremiumActivity
import com.hashmob.aichat.main.home.tab.chatbot.ChatActivity
import com.hashmob.aichat.main.home.tab.generateimage.GenerateImageActivity
import com.hashmob.aichat.main.home.tab.generateimage.generateimageresult.GenerateImageResultActivity
import com.hashmob.aichat.main.home.tab.scanmathsolving.ScanMathSolvingActivity
import com.hashmob.aichat.main.home.tab.scanmathsolving.solution.SolutionActivity
import com.hashmob.aichat.main.home.tab.topic.ui.CategoriesChatActivity
import com.hashmob.aichat.main.home.tab.topic.tab.composition.CompositionActivity
import com.hashmob.aichat.main.home.tab.topic.tab.composition.chooselanguage.ChooseLanguageActivity
import com.hashmob.aichat.main.home.tab.topic.tab.composition.result.CompositionResultActivity
import com.hashmob.aichat.main.home.ui.HomeActivity
import com.hashmob.aichat.main.home.ui.drawer.PrivacyPolicyActivity
import com.hashmob.aichat.main.intro.IntroActivity
import com.hashmob.aichat.main.splash.SplashActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class ActivityModuleBuilder {

    @MainScope
    @ContributesAndroidInjector()
    abstract fun contributeSplashActivity(): SplashActivity

    @MainScope
    @ContributesAndroidInjector()
    abstract fun contributeIntroActivity(): IntroActivity

    @MainScope
    @ContributesAndroidInjector()
    abstract fun contributeHomeActivity(): HomeActivity

    @MainScope
    @ContributesAndroidInjector()
    abstract fun contributeGenerateImageActivity(): GenerateImageActivity

    @MainScope
    @ContributesAndroidInjector()
    abstract fun contributeGenerateImageResultActivity(): GenerateImageResultActivity

    @MainScope
    @ContributesAndroidInjector()
    abstract fun contributePremiumActivity(): PremiumActivity

    @MainScope
    @ContributesAndroidInjector()
    abstract fun contributeCategoriesChatActivity(): CategoriesChatActivity

    @MainScope
    @ContributesAndroidInjector()
    abstract fun contributeCompositionActivity(): CompositionActivity

    @MainScope
    @ContributesAndroidInjector()
    abstract fun contributeCompositionResultActivity(): CompositionResultActivity

    @MainScope
    @ContributesAndroidInjector()
    abstract fun contributeChooseLanguageActivity(): ChooseLanguageActivity

    @MainScope
    @ContributesAndroidInjector()
    abstract fun contributeChatActivity(): ChatActivity

    @MainScope
    @ContributesAndroidInjector()
    abstract fun contributePrivacyPolicyActivity(): PrivacyPolicyActivity

    @MainScope
    @ContributesAndroidInjector()
    abstract fun contributeScanMathSolvingActivity(): ScanMathSolvingActivity

    @MainScope
    @ContributesAndroidInjector()
    abstract fun contributeSolutionActivity(): SolutionActivity
}