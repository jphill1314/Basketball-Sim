package com.appdev.jphil.basketballcoach.schedule.ui

import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Test

class ScheduleTransformerTest {

    private lateinit var transformer: ScheduleTransformer

    @Before
    fun setUp() {
        transformer = ScheduleTransformer()
    }

    @Test
    fun `default data state results in loading view state`() {
        val dataState = ScheduleContract.ScheduleDataState()
        val viewState = transformer.transform(dataState)

        assertTrue(viewState.isLoading)
    }
}