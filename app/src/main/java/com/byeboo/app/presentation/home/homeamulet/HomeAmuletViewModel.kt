package com.byeboo.app.presentation.home.homeamulet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.byeboo.app.core.designsystem.type.CustomSnackBarType
import com.byeboo.app.core.util.MixpanelUtil
import com.byeboo.app.domain.repository.auth.UserRepository
import com.byeboo.app.domain.repository.quest.QuestStateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
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
class HomeAmuletViewModel
    @Inject
    constructor(
        private val userRepository: UserRepository,
        private val questStateRepository: QuestStateRepository,
        private val mixpanelUtil: MixpanelUtil,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(HomeAmuletState())
        val uiState: StateFlow<HomeAmuletState> = _uiState.asStateFlow()

        private val _sideEffect = MutableSharedFlow<HomeAmuletSideEffect>()
        val sideEffect: SharedFlow<HomeAmuletSideEffect> = _sideEffect.asSharedFlow()

        init {
            fetchJourneyFromLocal()
        }

        private fun fetchJourneyFromLocal() {
            viewModelScope.launch {
                val localJourney: String? = questStateRepository.getUserJourney()
                if (localJourney != null) {
                    val amuletType = AmuletType.from(localJourney)
                    _uiState.update { it.copy(journey = amuletType) }
                }
            }
        }

        fun fetchJourneyFromServer() {
            viewModelScope.launch {
                userRepository
                    .getUserJourney()
                    .onSuccess { data ->
                        val amuletType = AmuletType.from(data.journey)

                        _uiState.update {
                            it.copy(
                                journey = amuletType,
                                journeyDescription = data.description,
                                canFlip = true,
                            )
                        }

                        mixpanelUtil.trackEvent(
                            eventName = "journey_card_complete",
                            properties =
                                mapOf(
                                    "journey_type" to amuletType.journeyName,
                                ),
                        )
                    }.onFailure {
                        _sideEffect.emit(
                            HomeAmuletSideEffect.ShowSnackBar(
                                message = "서버에 연결할 수 없습니다. 잠시 후 시도해 주세요.",
                                iconType = CustomSnackBarType.ALERT,
                            ),
                        )
                    }
            }
        }

        fun navigateToHomeOnboarding() {
            viewModelScope.launch {
                _sideEffect.emit(HomeAmuletSideEffect.NavigateToHomeOnboarding)
            }
        }
    }
