package com.example.scoreapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scoreapp.data.entity.MatchDetailResponse
import com.example.scoreapp.data.repository.FootballRepository
import com.example.scoreapp.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MatchDetailViewModel @Inject constructor(private val repository: FootballRepository) : ViewModel() {

    private val _matchDetail = MutableStateFlow<UiState<MatchDetailResponse>>(UiState.Loading)
    val matchDetail: StateFlow<UiState<MatchDetailResponse>> = _matchDetail

    fun loadMatchDetail(matchId: Int) {
        viewModelScope.launch {
            _matchDetail.value = UiState.Loading
            repository.getMatchDetail(matchId)
                .onSuccess { match ->
                    if (match == null) {
                        _matchDetail.value = UiState.Error("Maç bilgileri alınamadı.")
                    } else if (match.homeTeam == null || match.awayTeam == null) {
                        _matchDetail.value = UiState.Error("Eksik maç verisi alındı.")
                    } else {
                        _matchDetail.value = UiState.Success(match)
                    }
                }
                .onFailure { error ->
                    _matchDetail.value = UiState.Error(error.message ?: "Bilinmeyen bir hata oluştu.")
                }
        }
    }

}
