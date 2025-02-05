package com.example.scoreapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scoreapp.data.entity.Competition
import com.example.scoreapp.data.repository.FootballRepository
import com.example.scoreapp.retrofit.FootballApiService
import com.example.scoreapp.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LeaguesViewModel @Inject constructor(private val repository: FootballRepository) : ViewModel() {

    private val _competitions = MutableStateFlow<UiState<List<Competition>>>(UiState.Loading)
    val competitions: StateFlow<UiState<List<Competition>>> = _competitions

    init {
        loadCompetitions()
    }

    fun loadCompetitions() {
        viewModelScope.launch {
            _competitions.value = UiState.Loading
            repository.getCompetitions()
                .onSuccess { competitions ->
                    _competitions.value = UiState.Success(competitions)
                }
                .onFailure { error ->
                    _competitions.value = UiState.Error(error.message ?: "Bilinmeyen bir hata oluştu")
                }
        }
    }
}
