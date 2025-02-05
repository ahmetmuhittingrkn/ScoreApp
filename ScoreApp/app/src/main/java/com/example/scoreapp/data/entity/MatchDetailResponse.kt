package com.example.scoreapp.data.entity

import com.google.gson.annotations.SerializedName

data class MatchDetailResponse(
    @SerializedName("area") val matchArea: MatchArea?,
    val competition: Competition?,
    val season: Season?,
    val id: Int,
    val utcDate: String?,
    val status: String?,
    val venue: String?,
    val matchday: Int?,
    val stage: String?,
    val homeTeam: Team?,
    val awayTeam: Team?,

    @SerializedName("score") val score: MatchScore?,

    @SerializedName("referees") val referees: List<MatchReferee>?
)

data class MatchArea(
    val id: Int?,
    val name: String?,
    val code: String?,
    val flag: String?
)

data class MatchReferee(
    val id: Int?,
    val name: String?,
    val nationality: String?
)

data class MatchScore(
    val winner: String?,
    val duration: String?,
    val fullTime: MatchScoreDetail?,
    val halfTime: MatchScoreDetail?
)


data class MatchScoreDetail(
    val home: Int?,
    val away: Int?
)
