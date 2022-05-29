package com.example.linkswell.home.domain

import com.example.linkswell.db.entity.LinkEntity
import com.example.linkswell.home.data.datasource.ILocalLinksDataSource
import com.example.linkswell.home.data.models.appmodel.LinkGroupCollectionAppModel
import com.example.linkswell.home.data.models.appmodel.toLinkAppModel
import com.example.linkswell.home.data.repository.LinksRepository
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GetLinksGroupedInSameTypeListUseCaseTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var subjectUnderTest: GetLinksGroupedInSameTypeListUseCase

    private val localDataSource = mockk<ILocalLinksDataSource>()

    private val linksList1 = listOf(
        LinkEntity("origin-123", timestamp = 16004, groupName = "group1", extraNote = ""),
        LinkEntity("origin-124", timestamp = 16004, groupName = "group1", extraNote = ""),
        LinkEntity("origin-125", timestamp = 16004, groupName = "group3", extraNote = ""),
        LinkEntity("origin-126", timestamp = 16004, groupName = "group1", extraNote = ""),
        LinkEntity("origin-127", timestamp = 16004, groupName = "group2", extraNote = ""),
        LinkEntity("origin-127", timestamp = 160066, groupName = "group2", extraNote = ""),
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        subjectUnderTest = GetLinksGroupedInSameTypeListUseCase(
            linksRepository = LinksRepository(localDataSource)
        )

        coEvery {
            localDataSource.getAllLinks()
        } returns flowOf(
            linksList1
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `usecase on invoke - filters all links based on the group name and group them in their respective groups`() = runTest {
        val result = subjectUnderTest.invoke().first()
        val expectedResult = listOf(
            LinkGroupCollectionAppModel("group1", linksList1.filter { it.groupName == "group1" }.map { it.toLinkAppModel() }),
            LinkGroupCollectionAppModel("group2", linksList1.filter { it.groupName == "group2" }.map { it.toLinkAppModel() }),
            LinkGroupCollectionAppModel("group3", linksList1.filter { it.groupName == "group3" }.map { it.toLinkAppModel() })
        )

        assertEquals(expectedResult.toHashSet(), result.toHashSet())
    }
}