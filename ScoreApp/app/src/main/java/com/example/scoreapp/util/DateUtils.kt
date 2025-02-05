package com.example.scoreapp.util

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

    fun formatDate(utcDate: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            inputFormat.timeZone = TimeZone.getTimeZone("UTC")

            val outputFormat = SimpleDateFormat("dd MMMM yyyy - HH:mm", Locale.getDefault())
            val date = inputFormat.parse(utcDate)
            outputFormat.format(date ?: return "Bilinmeyen Tarih")
        } catch (e: Exception) {
            "Bilinmeyen Tarih"
        }
    }
