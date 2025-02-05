package com.example.scoreapp.data.entity

import com.google.gson.annotations.SerializedName

data class StandingsResponse(
    @SerializedName("standings") val standings: List<StandingTable>?
)

data class StandingTable(
    @SerializedName("stage") val stage: String?,
    @SerializedName("type") val type: String?,
    @SerializedName("table") val table: List<StandingTeam>?
)

data class StandingTeam(
    @SerializedName("position") val position: Int,
    @SerializedName("team") val team: Team,
    @SerializedName("playedGames") val playedGames: Int,
    @SerializedName("won") val won: Int,
    @SerializedName("draw") val draw: Int,
    @SerializedName("lost") val lost: Int,
    @SerializedName("points") val points: Int,
    @SerializedName("goalDifference") val goalDifference: Int
)
