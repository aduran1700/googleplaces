package com.googleplaces.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.googleplaces.data.model.Result

@Database(entities = [Result::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun resultDao(): ResultDao
}