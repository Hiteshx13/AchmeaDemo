package com.achmea.demo.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class EmployerDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var database: AppDatabase
    private lateinit var dao: EmployerDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.employerDao
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertShoppingItem() = runBlocking {
        val employer = EmployerEntity(
            1,
            12,
            "Employer 1",
            "Place 1",
            123
        )
        dao.insertAllEmployers(listOf(employer))
        val listEmployers = dao.getEmployersByFilterAndMaxRow("Emp", null)

        assertThat(listEmployers).contains(employer)
    }

    @Test
    fun deleteOldEmployers() = runBlocking {
        val currentTimestamp = System.currentTimeMillis()
        val employer = EmployerEntity(
            1,
            12,
            "Employer 1",
            "Place 1",
            currentTimestamp
        )
        dao.insertAllEmployers(listOf(employer))

        delay(7000)
        val afterTime = System.currentTimeMillis() - TimeUnit.SECONDS.toMillis(5)
        dao.deleteOldData(afterTime)

        val listEmployers = dao.getAllEmployers()
        assertThat(listEmployers).doesNotContain(employer)
    }

}