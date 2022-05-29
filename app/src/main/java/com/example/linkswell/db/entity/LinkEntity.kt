package com.example.linkswell.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "links_table")
data class LinkEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "original_link")
    val originalLink: String,
    @ColumnInfo(name = "timestamp")
    val timestamp: Long,
    @ColumnInfo(name = "group_name")
    val groupName: String,
    @ColumnInfo(name = "extra_note")
    val extraNote: String? = null
)
