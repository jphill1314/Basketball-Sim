package com.appdev.jphil.basketballcoach.newseason.ui

import app.cash.turbine.test
import com.appdev.jphil.basketballcoach.newseason.NewSeasonRepository
import com.appdev.jphil.basketballcoach.schedule.ui.SchedulePresenter
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NewSeasonPresenterTest {

    private val newSeasonState = MutableStateFlow(NewSeasonRepository.NewSeasonState())
    private val newSeasonRepository = mockk<NewSeasonRepository>(relaxed = true) {
        every { state } returns newSeasonState
    }
    private lateinit var presenter: NewSeasonPresenter

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        presenter = NewSeasonPresenter(newSeasonRepository)
    }

    @After
    fun cleanup() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init calls startNewSeason`() = runTest {
        coVerify { newSeasonRepository.startNewSeason() }
    }

    @Test
    fun `updated repo state updates data state`() = runTest {
        newSeasonState.value = NewSeasonRepository.NewSeasonState(stepNumber = 3)

        advanceUntilIdle()

        assertEquals(
            3,
            presenter.state.value.stepNumber
        )
    }

    @Test
    fun `transform adds StartNewSeasonModel when stepNumber greater than 4`() = runTest {
        val defaultDataState = NewSeasonContract.NewSeasonDataState()
        val defaultViewState = presenter.transform(defaultDataState)

        assertEquals(
            1,
            defaultViewState.uiModels.size,
        )

        val updatedDataState = NewSeasonContract.NewSeasonDataState(
            stepNumber = 5
        )
        val updatedViewState = presenter.transform(updatedDataState)

        assertEquals(
            2,
            updatedViewState.uiModels.size
        )
        assert(updatedViewState.uiModels.last() is StartNewSeasonModel)
    }

    @Test
    fun `startNewSeason sends StartNewSeasonEvent`() = runTest {
        presenter.events.test {
            presenter.startNewSeason()
            assert(awaitItem() is NewSeasonPresenter.StartNewSeasonEvent)
            cancelAndIgnoreRemainingEvents()
        }
    }
}