package com.example.weighttracker_brandonmullins.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "goals")
data class Goal(
    @PrimaryKey val username: String,
    val goalWeightLbs: Double
)
