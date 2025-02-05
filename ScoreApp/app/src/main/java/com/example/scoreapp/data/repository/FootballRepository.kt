package com.example.scoreapp.data.repository

import android.util.Log
import com.example.scoreapp.data.entity.Competition
import com.example.scoreapp.data.entity.Match
import com.example.scoreapp.data.entity.MatchDetailResponse
import com.example.scoreapp.data.entity.StandingTeam
import com.example.scoreapp.data.entity.Team
import com.example.scoreapp.retrofit.FootballApiService
import javax.inject.Inject

class FootballRepository @Inject constructor(private val apiService: FootballApiService) {

    suspend fun getCompetitions(): Result<List<Competition>> {
        return try {
            val response = apiService.getCompetitions()
            val competitions = response.competitions ?: emptyList()

            Result.success(competitions)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getLeagueDetail(leagueId: Int): Result<Competition> {
        return try {
            val response = apiService.getLeagueDetail(leagueId)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getTeams(leagueId: Int): Result<List<Team>> {
        return try {
            val response = apiService.getTeams(leagueId)
            Result.success(response.teams)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun getTeamDetail(teamId: Int): Result<Team> {
        return try {
            val response = apiService.getTeamDetail(teamId)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getTeamMatches(teamId: Int): Result<List<Match>> {
        return try {
            val response = apiService.getTeamMatches(teamId)
            Result.success(response.matches)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getMatchDetail(matchId: Int): Result<MatchDetailResponse> {
        return try {
            val response = apiService.getMatchDetail(matchId)

            Log.d("API_RESPONSE", "Match Detail Response: $response")

            if (response == null) {
                Result.failure(Exception("Maç detayları alınamadı."))
            } else {
                Result.success(response)
            }
        } catch (e: Exception) {
            Log.e("API_ERROR", "Hata oluştu: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun getLeagueStandings(leagueId: Int): Result<List<StandingTeam>> {
        return try {
            val response = apiService.getLeagueStandings(leagueId)
            val standings = response.standings?.firstOrNull()?.table ?: emptyList()
            Result.success(standings)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getLastMatchdayMatches(leagueId: Int): Result<List<Match>> {
        return try {
            val response = apiService.getCompetitionMatches(leagueId)
            val matches = response.matches

            val finishedMatches = matches.filter { it.status == "FINISHED" && it.matchday != null }

            val lastMatchday = finishedMatches.maxOfOrNull { it.matchday!! } ?: 0

            val lastMatches = finishedMatches.filter { it.matchday == lastMatchday }
            Result.success(lastMatches)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}