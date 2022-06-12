package com.example.linkswell.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.linkswell.home.data.models.appmodel.LinkAppModel

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

fun LinkAppModel.toLinkEntity(): LinkEntity {
    return LinkEntity(
        originalLink = originalLink,
        timestamp = timeStamp,
        groupName = groupName,
        extraNote = if (extraNote.isEmpty()) null else extraNote
    )
}
