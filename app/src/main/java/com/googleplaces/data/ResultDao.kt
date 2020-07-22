package com.googleplaces.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.googleplaces.data.model.Result
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface ResultDao {
    @Query("SELECT * FROM Result")
    fun getAll(): Single<List<Result>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(results: List<Result>): Completable
}