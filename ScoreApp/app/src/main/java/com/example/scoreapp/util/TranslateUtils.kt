package com.example.scoreapp.util

fun translateMatchStatus(status: String?): String {
    return when (status) {
        "FINISHED" -> "Bitti"
        "IN_PLAY" -> "Devam Ediyor"
        "PAUSED" -> "Durduruldu"
        "SCHEDULED" -> "Planlandı"
        "POSTPONED" -> "Ertelendi"
        else -> "Bilinmiyor"
    }
}

fun translateDuration(duration: String?): String {
    return when (duration) {
        "REGULAR" -> "Normal Süre"
        "EXTRA_TIME" -> "Uzatmalar"
        "PENALTIES" -> "Penaltılar"
        else -> "Bilinmiyor"
    }
}

fun translateWinner(winner: String?): String {
    return when (winner) {
        "HOME_TEAM" -> "Ev Sahibi Takım"
        "AWAY_TEAM" -> "Deplasman Takımı"
        "DRAW" -> "Beraberlik"
        else -> "Bilinmiyor"
    }
}
