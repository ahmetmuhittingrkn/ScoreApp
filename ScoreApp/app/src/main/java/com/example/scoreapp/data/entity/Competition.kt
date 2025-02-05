package com.example.scoreapp.data.entity

data class Competition(
    val id: Int,
    val area: Area,
    val name: String,
    val code: String,
    val emblem: String?
)

data class Area(
    val id: Int,
    val name: String
)

