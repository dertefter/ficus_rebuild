package com.dertefter.ficus.data.timetable

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface WeekDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeeks(weeks: List<Week>)

    @Update
    suspend fun updateWeek(week: Week)

    @Delete
    suspend fun deleteWeek(week: Week)

    @Query("SELECT * FROM weeks")
    suspend fun getWeeks(): List<Week>

    @Query("SELECT * FROM weeks WHERE weekQuery = :weekQuery")
    suspend fun getWeek(weekQuery: String): Week
}