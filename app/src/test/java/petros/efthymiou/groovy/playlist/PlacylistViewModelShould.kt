package petros.efthymiou.groovy.playlist

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

import org.junit.Assert.*
import petros.efthymiou.groovy.utils.BaseUnitTest
import petros.efthymiou.groovy.utils.captureValues
import petros.efthymiou.groovy.utils.getValueForTest
import java.lang.RuntimeException

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class PlacylistViewModelShould : BaseUnitTest() {
    private val repository: PlaylistRepository = mock()
    private val playlist = mock<List<Playlist>>()
    private val expected = Result.success(playlist)
    private val exception = RuntimeException("Something went wrong")

    @Test
    fun getPlaylistFromRepository() = runBlockingTest {
        val viewModel = mockSuccessCase()
        viewModel.playlist.getValueForTest()

        verify(repository, times(1)).getPlaylist()
    }

    @Test
    fun emitsPlaylistFromRepository() = runBlockingTest {
        val viewModel = mockSuccessCase()
        assertEquals(expected, viewModel.playlist.getValueForTest())
    }

    @Test
    fun emitErrorWhenReceiverError() = runBlockingTest {
        val viewModel = mockErrorCase()

        assertEquals(exception, viewModel.playlist.getValueForTest()!!.exceptionOrNull())
    }

    @Test
    fun showSpinnerWhileLoading() = runBlockingTest {
        val viewModel = mockSuccessCase()

        viewModel.loader.captureValues {
            viewModel.playlist.getValueForTest()
            assertEquals(true, values[0])
        }
    }

    @Test
    fun closeLoaderAfterPlaylistLoad() = runBlockingTest {
        val viewModel = mockSuccessCase()

        viewModel.loader.captureValues {
            viewModel.playlist.getValueForTest()

            assertEquals(false, values.last())
        }
    }

    @Test
    fun closeLoaderAfterError() = runBlockingTest {
        val viewModel = mockErrorCase()

        viewModel.loader.captureValues {
            viewModel.playlist.getValueForTest()

            assertEquals(false, values.last())
        }
    }

    private fun mockSuccessCase(): PlaylistViewModel {
        runBlocking {
            whenever(repository.getPlaylist()).thenReturn(
                flow {
                    emit(expected)
                }
            )
        }
        return PlaylistViewModel(repository)
    }


    private suspend fun mockErrorCase(): PlaylistViewModel {
        whenever(repository.getPlaylist())
            .thenReturn(
                flow {
                    emit(Result.failure<List<Playlist>>(exception))
                }
            )
        return PlaylistViewModel(repository)
    }
}