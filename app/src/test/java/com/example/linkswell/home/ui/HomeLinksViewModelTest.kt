package com.example.linkswell.home.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.linkswell.MainCoroutineScopeRule
import com.example.linkswell.home.HomeLinksIntent
import com.example.linkswell.home.HomeLinksSideEffect
import com.example.linkswell.home.HomeLinksViewState
import com.example.linkswell.home.data.models.appmodel.LinkGroupCollectionAppModel
import com.example.linkswell.home.domain.GetLinksGroupedInSameTypeListUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

@ExperimentalCoroutinesApi
class HomeLinksViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineScopeRule: MainCoroutineScopeRule = MainCoroutineScopeRule()

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var viewModel: HomeLinksViewModel

    private val getLinksGroupedInSameTypeListUseCase = mockk<GetLinksGroupedInSameTypeListUseCase>()

    @Before
    fun setUp() {
        viewModel = HomeLinksViewModel(
            getLinksGroupedInSameTypeListUseCase = getLinksGroupedInSameTypeListUseCase
        )

        coEvery {
            getLinksGroupedInSameTypeListUseCase()
        } returns flowOf(
            listOf(
                LinkGroupCollectionAppModel("group1", listOf()),
                LinkGroupCollectionAppModel("group2", listOf()),
                LinkGroupCollectionAppModel("group3", listOf()),
            )
        )
    }


    @Test
    fun `viewState starts with initial value`() = runTest {
        assertEquals(HomeLinksViewState(), viewModel.viewState.first())
    }

    @Test
    fun `on viewCreated intent - first loading is set to true and then ui updates with link group data`() = runTest {
        val stateList = mutableListOf<HomeLinksViewState>()
        val stateJob = launch(testDispatcher) {
            viewModel.viewState.collect(stateList::add)
        }

        viewModel.handleIntent(HomeLinksIntent.ViewCreated)
        val linkGroupList = listOf(
            LinkGroupCollectionAppModel("group1", listOf()),
            LinkGroupCollectionAppModel("group2", listOf()),
            LinkGroupCollectionAppModel("group3", listOf()),
        )
        stateJob.cancel()

        println(stateList)
        assertEquals(3, stateList.size)
        assertEquals(HomeLinksViewState(), stateList[0])
        assertEquals(HomeLinksViewState(isLoading = true), stateList[1])
        assertEquals(HomeLinksViewState(isLoading = false, linkGroups = linkGroupList), stateList[2])
    }

    @Test
    fun `on viewCreated intent - if there are no link groups - then error msg is shown`() = runTest {

        coEvery {
            getLinksGroupedInSameTypeListUseCase.invoke()
        } returns flowOf(listOf())

        val stateList = mutableListOf<HomeLinksViewState>()
        val stateJob = launch(testDispatcher) {
            viewModel.viewState.collect(stateList::add)
        }

        viewModel.handleIntent(HomeLinksIntent.ViewCreated)
        stateJob.cancel()

        println(stateList)
        assertEquals(true, stateList.last().shouldShowNoResultsMsg)
    }

    @Test
    fun `on link group clicked - navigate to details list effect is emitted`() = runTest {
        val effectsList = mutableListOf<HomeLinksSideEffect>()
        val effectJob = launch(testDispatcher) {
            viewModel.sideEffects.collect(effectsList::add)
        }

        val clickedGroup = LinkGroupCollectionAppModel(
            groupName = "test",
            links = listOf()
        )

        viewModel.handleIntent(HomeLinksIntent.LinkGroupClicked(clickedGroup))

        effectJob.cancel()

        assertEquals(1, effectsList.size)
        assertEquals(HomeLinksSideEffect.NavigateToGroupLinks(clickedGroup), effectsList[0])
    }
}