package com.byeboo.app.presentation.auth.onboarding

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.byeboo.app.R
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

@HiltViewModel
class OnboardingViewModel @Inject constructor() : ViewModel() {
    private val _pageIndex = mutableIntStateOf(0)
    val pageIndex: State<Int> = _pageIndex

    private val _sideEffect = MutableSharedFlow<OnboardingSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    val pageNumber: String
        get() = "${_pageIndex.intValue + 1}/${pages.size}"

    private val pages: PersistentList<PersistentList<OnboardingState>> = persistentListOf(
        persistentListOf(
            OnboardingState(
                title = "저는 당신이 털어놓은 감정을 담는 보따리,\n보리라고 해요.",
                imageRes = R.drawable.img_onboarding_1st
            ),

            OnboardingState(
                title = "이별 후 걸림돌 같은 감정들을 털어놔 주시면\n제 안에서 감정돌이 되어 쌓여요.",
                imageRes = R.drawable.img_onboarding_2nd
            )
        ),

        persistentListOf(
            OnboardingState(
                title = "당신이 준비가 되었을 때\n감정돌들을 하나씩 꺼내 바닥에 놓아드려요.",
                imageRes = R.drawable.img_onboarding_3rd
            ),

            OnboardingState(
                title = "제가 모아둔 감정돌을 디딤돌 삼아\n한 걸음 한 걸음 미래로 나아가주세요.",
                imageRes = R.drawable.img_onboarding_4th
            )
        ),

        persistentListOf(
            OnboardingState(
                title = "자, 이제 시간이 됐어요.\n\n" +
                    "당신에게 꼭 맞는 이별 극복 여정에 따라\n" +
                    "퀘스트를 하나하나씩 진행하면서\n" +
                    "감정을 정리하고, 극복해 보아요.\n\n" +
                    "저 보리가 항상 함께할게요.",
                imageRes = R.drawable.img_onboarding_5th
            )
        )
    )

    fun onNextPage() {
        if (_pageIndex.intValue == 2) {
            viewModelScope.launch {
                _sideEffect.emit(OnboardingSideEffect.NavigationToUserInfo)
            }

        } else {
            nextPage()
        }
    }

    fun nextPage() {
        if (_pageIndex.intValue < pages.lastIndex) {
            _pageIndex.intValue += 1
        }
    }

    fun previousPage() {
        if (_pageIndex.intValue > 0) {
            _pageIndex.intValue -= 1
        }
    }

    fun skipPage() {
        viewModelScope.launch {
            _sideEffect.emit(OnboardingSideEffect.NavigationToUserInfo)
        }
    }

    fun currentContents(): PersistentList<OnboardingState> = pages[_pageIndex.intValue]

}
