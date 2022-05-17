package com.proto.dynamiclayout.database.dao

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Query
import androidx.room.RawQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.proto.dynamiclayout.database.BaseDao
import com.proto.dynamiclayout.database.entity.CartEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao : BaseDao<CartEntity> {

    @RawQuery(observedEntities = [CartEntity::class])
    fun getSongs(query: SupportSQLiteQuery): Cursor
}