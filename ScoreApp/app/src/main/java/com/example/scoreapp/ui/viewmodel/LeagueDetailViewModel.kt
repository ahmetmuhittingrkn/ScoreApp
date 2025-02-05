package com.example.scoreapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scoreapp.data.entity.Competition
import com.example.scoreapp.data.entity.StandingTeam
import com.example.scoreapp.data.entity.Team
import com.example.scoreapp.data.entity.Match
import com.example.scoreapp.data.repository.FootballRepository
import com.example.scoreapp.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LeagueDetailViewModel @Inject constructor(private val repository: FootballRepository) : ViewModel() {

    private val _leagueDetail = MutableStateFlow<UiState<Competition>>(UiState.Loading)
    val leagueDetail: StateFlow<UiState<Competition>> = _leagueDetail

    private val _teams = MutableStateFlow<UiState<List<Team>>>(UiState.Loading)
    val teams: StateFlow<UiState<List<Team>>> = _teams

    private val _standings = MutableStateFlow<UiState<List<StandingTeam>>>(UiState.Loading)
    val standings: StateFlow<UiState<List<StandingTeam>>> = _standings

    private val _lastMatches = MutableStateFlow<UiState<List<Match>>>(UiState.Loading)
    val lastMatches: StateFlow<UiState<List<Match>>> = _lastMatches

    fun loadLeagueDetail(leagueId: Int) {
        viewModelScope.launch {
            _leagueDetail.value = UiState.Loading
            repository.getLeagueDetail(leagueId)
                .onSuccess { league ->
                    _leagueDetail.value = UiState.Success(league)
                }
                .onFailure { error ->
                    _leagueDetail.value = UiState.Error(error.message ?: "Hata oluştu")
                }
        }
    }

    fun loadTeams(leagueId: Int) {
        viewModelScope.launch {
            _teams.value = UiState.Loading
            repository.getTeams(leagueId)
                .onSuccess { teamList ->
                    _teams.value = UiState.Success(teamList)
                }
                .onFailure { error ->
                    _teams.value = UiState.Error(error.message ?: "Takımlar yüklenirken hata oluştu")
                }
        }
    }

    fun loadStandings(leagueId: Int) {
        viewModelScope.launch {
            _standings.value = UiState.Loading
            repository.getLeagueStandings(leagueId)
                .onSuccess { standingsList ->
                    _standings.value = UiState.Success(standingsList)
                }
                .onFailure { error ->
                    _standings.value = UiState.Error(error.message ?: "Puan durumu yüklenemedi.")
                }
        }
    }

    fun loadLastMatches(leagueId: Int) {
        viewModelScope.launch {
            _lastMatches.value = UiState.Loading
            repository.getLastMatchdayMatches(leagueId)
                .onSuccess { matches ->
                    _lastMatches.value = UiState.Success(matches)
                }
                .onFailure { error ->
                    _lastMatches.value = UiState.Error(error.message ?: "Maçlar yüklenirken hata oluştu")
                }
        }
    }
}
