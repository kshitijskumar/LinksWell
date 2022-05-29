package com.example.linkswell.db.dao

import androidx.room.*
import com.example.linkswell.db.entity.LinkEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LinksDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLinkEntity(entity: LinkEntity)

    @Query("SELECT * FROM links_table WHERE group_name =:groupName ORDER BY timestamp DESC")
    fun getAllLinksForGroupName(groupName: String): Flow<List<LinkEntity>>

    @Delete
    fun deleteLinkDetails(linkToDelete: LinkEntity)

    @Query("SELECT * FROM links_table WHERE original_link = :link")
    fun getLinkDetailsFromLink(link: String): Flow<LinkEntity?>

}