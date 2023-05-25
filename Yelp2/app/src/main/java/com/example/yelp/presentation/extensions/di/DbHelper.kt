package com.example.yelp.presentation.extensions.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.yelp.data.localSource.db.Database
import com.example.yelp.data.localSource.db.dao.ReservationsDao


/**
 * Factory method of creating RoomDatabase by specific class com.spg.mi.data.localSource.db.Database
 * and DB name and also In this file we need to maintain all DB migration schemas.
 */
fun provideYelpDatabase(appContext: Context): Database =
    Room.databaseBuilder(
        appContext,
        Database::class.java, "yelp_db"
    )
        .addMigrations(SPG_DB_MIGRATION_1_2)
        .build()

private val SPG_DB_MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            """
                CREATE TABLE IF NOT EXISTS `reservations` (
                    `reservationKey` TEXT PRIMARY KEY AUTOINCREMENT NOT NULL,
                    `date` TEXT NOT NULL,
                    `time` TEXT NOT NULL,
                    `email` TEXT NOT NULL,
                    `business` TEXT NOT NULL
                );
            """
        )
    }
}


/**
 * Returns prepared/generated DAO for communication with Menu entity
 */
fun provideReservationRoomDao(database: Database): ReservationsDao = database.reservationDao()

