package petros.efthymiou.groovy.playlist

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import petros.efthymiou.groovy.utils.BaseUnitTest

class PlaylistRepositoryShould : BaseUnitTest() {

    private val service : PlaylistService = mock()
    private val playlist = mock<List<Playlist>>()
    private val exception = RuntimeException("something went wrong")

    @Test
    fun getPlaylistFromService()  = runBlockingTest {

        val repository = PlaylistRepository(service)

        repository.getPlaylist()

        verify(service, times(1)).fetchPlaylists()
    }

    @Test
    fun emitPlaylistFromService()  = runBlockingTest {
        val repository = mockSuccessfulCase()

        assertEquals(playlist, repository.getPlaylist().first().getOrNull())
    }

    @Test
    fun propagateErrors() = runBlockingTest {
        val repository = mockFailuerCase()

        assertEquals(exception , repository.getPlaylist().first().exceptionOrNull())
    }

    private suspend fun mockFailuerCase(): PlaylistRepository {
        whenever(service.fetchPlaylists()).thenReturn(
            flow {
                emit(Result.failure<List<Playlist>>(exception))
            }
        )
        return PlaylistRepository(service)
    }

    private suspend fun mockSuccessfulCase(): PlaylistRepository {
        whenever(service.fetchPlaylists()).thenReturn(
            flow {
                emit(Result.success(playlist))
            }
        )

        return PlaylistRepository(service)
    }
}