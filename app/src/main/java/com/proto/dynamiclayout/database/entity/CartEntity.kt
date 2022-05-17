package com.proto.dynamiclayout.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class CartEntity(
    @PrimaryKey val promoId: String,
    val promoName: String,
    val principalId: String,
    val isSelected: Boolean
)