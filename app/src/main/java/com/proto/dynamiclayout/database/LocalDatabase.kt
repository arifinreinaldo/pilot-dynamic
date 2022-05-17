package com.proto.dynamiclayout.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.proto.dynamiclayout.database.dao.CartDao
import com.proto.dynamiclayout.database.entity.CartEntity

@Database(
    entities = [
        CartEntity::class
    ],
    version = 6, exportSchema = true,
//    autoMigrations = [AutoMigration(from = 2, to = 3)]
)

abstract class LocalDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao
}
