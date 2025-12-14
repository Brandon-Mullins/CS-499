package com.example.weighttracker_brandonmullins.data

import androidx.room.*

@Dao
interface AppDao {
    // Users
    @Query("SELECT * FROM users WHERE username = :u LIMIT 1")
    suspend fun getUser(u: String): User?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertUser(user: User)

    // Weights (CRUD)
    @Insert
    suspend fun insertWeight(entry: WeightEntry)

    @Update
    suspend fun updateWeight(entry: WeightEntry)

    @Delete
    suspend fun deleteWeight(entry: WeightEntry)

    @Query("SELECT * FROM weights WHERE username = :u ORDER BY date DESC, id DESC")
    suspend fun listWeights(u: String): List<WeightEntry>

    // Goal
    @Query("SELECT * FROM goals WHERE username = :u LIMIT 1")
    suspend fun getGoal(u: String): Goal?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertGoal(goal: Goal)
}
