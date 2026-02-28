package com.byeboo.app.presentation.quest.review.common.personal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.byeboo.app.presentation.quest.component.type.MyPostOption
import com.byeboo.app.presentation.quest.model.MyAnswerModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyAnswerViewModel
    @Inject
    constructor() : ViewModel() {
        private val _uiState = MutableStateFlow(MyAnswerState())
        val uiState: StateFlow<MyAnswerState> = _uiState.asStateFlow()

        private val _sideEffect = MutableSharedFlow<MyAnswerSideEffect>()
        val sideEffect: SharedFlow<MyAnswerSideEffect> = _sideEffect.asSharedFlow()

        init {
            _uiState.update { it.copy(answers = getDummyAnswers()) }
        }

        private fun getDummyAnswers(): ImmutableList<MyAnswerModel> =
            persistentListOf(
                MyAnswerModel(
                    answerId = 1,
                    question = "이별 후 제일 힘들었던 순간은?",
                    writtenAt = "2026-01-30",
                    content =
                        "헤어진 지 벌써 일주일이 지났습니다. 처음에는 실감이 안 나서 눈물조차 나오지 않았어요. 그저 멍하니 천장만 바라보며 시간을 보냈습니다. 그런데 오늘 아침, 습관적으로 휴대폰을 확인하다가 더 이상 '굿모닝' 인사를 보낼 사람이 없다는 사실을 깨닫고 그제야 무너져 내렸습니다. 밥알이 모래알 같아서 잘 넘어가지도 않네요. 친구들은 시간이 약이라고, 더 좋은 사람 만날 거라고 위로하지만 지금 당장은 그 어떤 말도 귀에 들어오지 않습니다.\n" +
                            "\n" +
                            "우리가 함께 걷던 거리, 자주 가던 카페, 이어폰을 나눠 끼고 듣던 노래들... 세상은 여전히 그대로 굴러가는데 우리 사이만 끊어졌다는 게 도무지 믿기지가 않아요. 당신이 사무치게 밉다가도, 당장이라도 달려가 안기고 싶은 내 마음이 너무 싫습니다. 수백 번 차단했다 풀었다를 반복하며 당신의 프로필을 훔쳐보는 내가 너무 초라해 보여요.\n" +
                            "\n" +
                            "이제는 인정해야겠죠. 우리는 끝났고, 나는 혼자가 되었다는 사실을요. 오늘은 억지로라도 밖으로 나가 햇볕을 쬐었습니다.",
                ),
                MyAnswerModel(
                    answerId = 2,
                    question = "이별 후 제일 힘들었던 순간은2?",
                    writtenAt = "2026-01-30",
                    content = "헤어진 첫날 밤이었어요. 혼자 집에 있는데 갑자기 모든 게 현실로 다가왔고, 이제 정말 끝이라는 생각에 눈물이 멈추지 않았습니다.",
                ),
                MyAnswerModel(
                    answerId = 3,
                    question = "이별 후 제일 힘들었던 순간은3?",
                    writtenAt = "2026-01-30",
                    content = "헤어진 첫날 밤이었어요. 혼자 집에 있는데 갑자기 모든 게 현실로 다가왔고, 이제 정말 끝이라는 생각에 눈물이 멈추지 않았습니다.",
                ),
                MyAnswerModel(
                    answerId = 4,
                    question = "이별 후 제일 힘들었던 순간은4?",
                    writtenAt = "2026-01-30",
                    content = "헤어진 첫날 밤이었어요. 혼자 집에 있는데 갑자기 모든 게 현실로 다가왔고, 이제 정말 끝이라는 생각에 눈물이 멈추지 않았습니다.",
                ),
            )

        fun onMyAnswerContentClick(answerId: Long) {
            viewModelScope.launch {
                _sideEffect.emit(MyAnswerSideEffect.NavigateToQuestMyAnswerDetail(answerId))
            }
        }

        fun onClickMoreOptions() {
            _uiState.update { it.copy(showBottomSheet = true) }
        }

        fun onDismissBottomSheet() {
            _uiState.update { it.copy(showBottomSheet = false) }
        }

        fun onOptionClick(option: MyPostOption) {
            onDismissBottomSheet()

            viewModelScope.launch {
                when (option) {
                    MyPostOption.EDIT -> { /* TODO 수정 화면 이동 */ }
                    MyPostOption.DELETE -> { /* TODO 삭제 모달 띄우기 */ }
                }
            }
        }
    }
