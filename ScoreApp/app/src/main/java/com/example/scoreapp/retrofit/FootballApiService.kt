package com.example.scoreapp.retrofit

import com.example.scoreapp.data.entity.Competition
import com.example.scoreapp.data.entity.CompetitionResponse
import com.example.scoreapp.data.entity.MatchDetailResponse
import com.example.scoreapp.data.entity.MatchResponse
import com.example.scoreapp.data.entity.StandingsResponse
import com.example.scoreapp.data.entity.Team
import com.example.scoreapp.data.entity.TeamResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface FootballApiService {

    @GET("competitions")
    suspend fun getCompetitions(): CompetitionResponse

    @GET("competitions/{id}")
    suspend fun getLeagueDetail(@Path("id") leagueId: Int): Competition

    @GET("competitions/{id}/teams")
    suspend fun getTeams(@Path("id") leagueId: Int): TeamResponse

    @GET("teams/{id}")
    suspend fun getTeamDetail(@Path("id") teamId: Int): Team

    @GET("teams/{id}/matches")
    suspend fun getTeamMatches(@Path("id") teamId: Int): MatchResponse

    @GET("matches/{id}")
    suspend fun getMatchDetail(@Path("id") matchId: Int): MatchDetailResponse

    @GET("competitions/{id}/standings")
    suspend fun getLeagueStandings(@Path("id") leagueId: Int): StandingsResponse

    @GET("competitions/{id}/matches")
    suspend fun getCompetitionMatches(@Path("id") leagueId: Int): MatchResponse
}