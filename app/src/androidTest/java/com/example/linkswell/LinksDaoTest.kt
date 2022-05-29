package com.example.linkswell

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.linkswell.db.AppDatabase
import com.example.linkswell.db.entity.LinkEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LinksDaoTest {

    private lateinit var db: AppDatabase

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context,
            AppDatabase::class.java
        ).allowMainThreadQueries()
            .build()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun insertLinkEntity_insertsEntityWhenNoConflicts() = runBlocking {
        with(db.linksDao()) {
            val linkEntityToInsert = LinkEntity(
                originalLink = "https://someLink.com",
                timestamp = 16000,
                groupName = "someLink"
            )

            insertLinkEntity(linkEntityToInsert)

            val linkEntityInserted = getAllLinksForGroupName("someLink").first()

            assertEquals(1, linkEntityInserted.size)
            assertEquals(linkEntityToInsert, linkEntityInserted[0])
        }
    }

    @Test
    fun insertLinkEntity_insertsEntityOnConflict_replacesTheOldValueWithNewValue() = runBlocking {
        with(db.linksDao()) {
            val linkEntityToInsert1 = LinkEntity(
                originalLink = "https://someLink.com",
                timestamp = 16000,
                groupName = "someLink"
            )

            insertLinkEntity(linkEntityToInsert1)

            val linkEntityInserted = getAllLinksForGroupName("someLink").first()

            assertEquals(1, linkEntityInserted.size)
            assertEquals(linkEntityToInsert1, linkEntityInserted[0])

            val updatedEntity = linkEntityToInsert1.copy(extraNote = "test note")

            insertLinkEntity(updatedEntity)

            val linkAfterUpdate = getAllLinksForGroupName("someLink").first()

            assertEquals(1, linkAfterUpdate.size)
            assertEquals(updatedEntity, linkAfterUpdate[0])
        }
    }

    @Test
    fun deleteLinkDetails_getLinkDetailsFromLink() = runBlocking {
        with(db.linksDao()) {
            val linkEntityToInsert1 = LinkEntity(
                originalLink = "https://someLink.com",
                timestamp = 16000,
                groupName = "someLink"
            )

            insertLinkEntity(linkEntityToInsert1)

            val entityReturned = getLinkDetailsFromLink(linkEntityToInsert1.originalLink).first()

            assertNotNull(entityReturned)

            deleteLinkDetails(linkEntityToInsert1)

            val entityReturnedAfterDelete = getLinkDetailsFromLink(linkEntityToInsert1.originalLink).first()

            assertNull(entityReturnedAfterDelete)
        }
    }
}