package com.example.weighttracker_brandonmullins.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weights")
data class WeightEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val username: String,      // owner
    val date: String,          // e.g. "2025-08-19"
    val weightLbs: Double
)
