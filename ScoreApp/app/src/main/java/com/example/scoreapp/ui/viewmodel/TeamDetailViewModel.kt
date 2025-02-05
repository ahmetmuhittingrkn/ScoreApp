package com.example.scoreapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scoreapp.data.entity.Match
import com.example.scoreapp.data.entity.Team
import com.example.scoreapp.data.repository.FootballRepository
import com.example.scoreapp.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeamDetailViewModel @Inject constructor(private val repository: FootballRepository) : ViewModel() {

    private val _teamDetail = MutableStateFlow<UiState<Team>>(UiState.Loading)
    val teamDetail: StateFlow<UiState<Team>> = _teamDetail

    private val _teamMatches = MutableStateFlow<UiState<List<Match>>>(UiState.Loading)
    val teamMatches: StateFlow<UiState<List<Match>>> = _teamMatches

    fun loadTeamDetail(teamId: Int) {
        viewModelScope.launch {
            _teamDetail.value = UiState.Loading
            repository.getTeamDetail(teamId)
                .onSuccess { team ->
                    _teamDetail.value = UiState.Success(team)
                }
                .onFailure { error ->
                    _teamDetail.value = UiState.Error(error.message ?: "Hata oluştu")
                }
        }
    }

    fun loadTeamMatches(teamId: Int) {
        viewModelScope.launch {
            _teamMatches.value = UiState.Loading
            repository.getTeamMatches(teamId)
                .onSuccess { matches ->
                    _teamMatches.value = UiState.Success(matches)
                }
                .onFailure { error ->
                    _teamMatches.value = UiState.Error(error.message ?: "Maçlar yüklenirken hata oluştu")
                }
        }
    }
}

