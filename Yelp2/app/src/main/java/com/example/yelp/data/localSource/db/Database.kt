package com.example.yelp.data.localSource.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.yelp.data.localSource.db.dao.ReservationsDao
import com.example.yelp.data.localSource.db.entities.ReservationsEntity

@Database(
    entities = [ReservationsEntity::class],
    version = 2,
    exportSchema = false
)

@TypeConverters(Converters::class)
abstract class Database : RoomDatabase() {

    abstract fun reservationDao(): ReservationsDao

}