package com.jay.fitnesstracker.roomdatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "activity_sessions")
data class ActivitySessionsDatabase(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "stepsByUser")
    val stepsByUser: Int,
    @ColumnInfo(name = "distanceInMeter")
    val distanceInMeter: Float,
    @ColumnInfo(name = "timeInSecond")
    val timeInSecond: Int,
    @ColumnInfo(name = "caloriesBurned")
    val caloriesBurned: Float
)