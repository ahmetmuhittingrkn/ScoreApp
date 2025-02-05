package com.example.scoreapp.data.entity

data class Match(
    val id: Int,
    val competition: Competition,
    val season: Season,
    val utcDate: String,
    val status: String,
    val matchday: Int?,
    val homeTeam: Team,
    val awayTeam: Team,
    val score: Score?
)

data class Score(
    val fullTime: ScoreDetail?,
    val halfTime: ScoreDetail?
)

data class ScoreDetail(
    val home: Int?,
    val away: Int?
)
