package com.byeboo.app.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.byeboo.app.domain.model.JourneyStatusType
import com.byeboo.app.domain.repository.quest.QuestStateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class MainViewModel @Inject constructor(
    private val questStateRepository: QuestStateRepository
) : ViewModel() {
    val journeyStatus: StateFlow<JourneyStatusType> = questStateRepository.getUserJourneyStatus()
        .stateIn(viewModelScope, SharingStarted.Eagerly, JourneyStatusType.UNKNOWN)
}
