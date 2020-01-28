package com.blauhaus.android.redwood.sample.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.blauhaus.android.redwood.lastfourweeks.views.DayView
import com.blauhaus.android.redwood.lastfourweeks.views.DayView.ViewState
import com.blauhaus.android.redwood.sample.data.IRepository
import com.blauhaus.android.redwood.sample.components.meditationchallenge.achievementpager.AchievementViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException


class AchievmentViewModelTest {
    private val mockRepo = MockRepo()
    private val model =
        AchievementViewModel(
            mockRepo
        )
    private val testData = listOf(Pair(0f, ""), Pair(15f, ""), Pair(0f, ""))


    // Required to get testing internals to work.
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun before() {
        mockRepo.meditationData().value = testData
    }

    @Test
    fun getLastFourWeeksBackingModel() {

        // generate expected results
        fun resultExpected(): MutableList<DayView.ViewState> {
            val r = mutableListOf(ViewState.Skipped(), ViewState.Met(), ViewState.Skipped())
            r.add(ViewState.DidntMeetYetToday())
            while(r.size < 28) {
                r.add(ViewState.Future())
            }
            return r
        }

        // get value from the LD
        val result = model.lastFourWeeksBackingModel.getOrAwaitValue()

        // simple check
        assertEquals(result.size, resultExpected().size)

        //  iterative check of ViewState
        val err = "Failure index %d %s != %s"
        result.zip(resultExpected()).forEachIndexed{index, p ->
            assertTrue(
                String.format(err, index, p.first::class, p.second::class),
                p.first::class == p.second::class)
        }


    }
}


//Google convenience extension for testing LiveData
/* Copyright 2019 Google LLC.
   SPDX-License-Identifier: Apache-2.0 */
fun <T> LiveData<T>.getOrAwaitValue(
    time: Long = 2,
    timeUnit: TimeUnit = TimeUnit.SECONDS
): T {
    var data: T? = null
    val latch = CountDownLatch(1)
    val observer = object : Observer<T> {
        override fun onChanged(o: T?) {
            data = o
            latch.countDown()
            this@getOrAwaitValue.removeObserver(this)
        }
    }

    this.observeForever(observer)

    // Don't wait indefinitely if the LiveData is not set.
    if (!latch.await(time, timeUnit)) {
        throw TimeoutException("LiveData value was never set.")
    }

    @Suppress("UNCHECKED_CAST")
    return data as T
}

class MockRepo: IRepository {
    private val _meditationData = MutableLiveData<List<Pair<Float, String>>>()
    override fun meditationData(): MutableLiveData<List<Pair<Float, String>>> {
        return _meditationData
    }
}