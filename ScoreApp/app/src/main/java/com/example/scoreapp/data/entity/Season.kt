package com.example.scoreapp.data.entity

data class Season(
    val id: Int,
    val startDate: String,
    val endDate: String,
    val currentMatchday: Int?
)