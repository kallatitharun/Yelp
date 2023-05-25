package com.example.yelp.data.localSource.db.dao

import androidx.room.*
import com.example.yelp.data.localSource.db.entities.ReservationsEntity

@Dao
interface ReservationsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun save(reservationEntity: List<ReservationsEntity>)

    @Query("SELECT * FROM reservations")
    suspend fun obtainReservations(): List<ReservationsEntity>

    @Delete
    suspend fun remove(reservationsEntity: ReservationsEntity)
}