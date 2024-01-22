package com.achmea.demo.presentation.viewmodel

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import com.achmea.demo.common.Constants
import com.achmea.demo.common.DataState
import com.achmea.demo.data.local.AppDatabase
import com.achmea.demo.data.local.EmployerDao
import com.achmea.demo.data.remote.EmployerApi
import com.achmea.demo.data.repository.EmployerRepositoryImpl
import com.achmea.demo.domain.model.Employer
import com.achmea.demo.domain.use_case.GetEmployerUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.doAnswer
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@ExperimentalCoroutinesApi
class DashboardViewModelTest {

    // This rule ensures that the architecture components' background tasks run on the same thread
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var database: AppDatabase
    private lateinit var employerDao: EmployerDao
    private val employerApi: EmployerApi by lazy {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .build()

        Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(EmployerApi::class.java)
    }

    // This dispatcher is used to control the execution of coroutines in tests
    private val testDispatcher = TestCoroutineDispatcher()

    private lateinit var getEmployerUseCase: GetEmployerUseCase

    @Mock
    private lateinit var employerDataObserver: Observer<DataState<List<Employer>>>

    private lateinit var employerRepository: EmployerRepositoryImpl

    private lateinit var viewModel: DashboardViewModel

    lateinit var instrumentationContext: Context

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        employerDao = database.employerDao
        instrumentationContext = InstrumentationRegistry.getInstrumentation().context

        employerRepository =
            EmployerRepositoryImpl(employerApi, employerDao)

        // Create a real instance of GetEmployerUseCase
        getEmployerUseCase = GetEmployerUseCase(employerRepository)

        viewModel = DashboardViewModel(getEmployerUseCase)
        viewModel.employerData.observeForever(employerDataObserver)

        // Dispatchers.setMain should be called in setup to override the main dispatcher for tests
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun getEmployers_shouldUpdateLiveDataWithSuccessState() = runTest {
        // Arrange
        viewModel.employerData.observeForever(employerDataObserver)

        val testData = listOf(
            Employer(
                employerID = 1,
                name = "Test Employer",
                discountPercentage = 10,
                place = "Place1",
                timestamp = 123
            )
        )
        val filter = "test"
        val maxRows = 10

        // Mock repository response using doAnswer
        doAnswer {
            DataState.Success(testData)
        }.`when`(getEmployerUseCase).getEmployers(filter, maxRows)

        // Act
        viewModel.getEmployers(filter, maxRows)

        // Assert
        verify(employerDataObserver).onChanged(DataState.Loading)
        verify(employerDataObserver).onChanged(DataState.Success(testData))
    }

    @Test
    fun getEmployers_shouldUpdateLiveDataWithErrorState() = runTest {
        // Arrange
        val filter = "test"
        val maxRows = 10
        val errorMessage = "An error occurred"

        // Mock repository response
        doAnswer {
            DataState.Error(errorMessage)
        }.`when`(getEmployerUseCase).getEmployers(filter, maxRows)

        // Act
        viewModel.getEmployers(filter, maxRows)

        // Assert
        verify(employerDataObserver).onChanged(DataState.Loading)
        verify(employerDataObserver).onChanged(DataState.Error(errorMessage))
    }
}
