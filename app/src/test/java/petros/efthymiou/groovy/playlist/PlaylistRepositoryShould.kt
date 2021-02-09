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
    private val mapper : PlaylistMapper = mock()
    private val playlist = mock<List<Playlist>>()
    private val playlistsRaw = mock<List<PlaylistRaw>>()
    private val exception = RuntimeException("something went wrong")

    @Test
    fun getPlaylistFromService()  = runBlockingTest {

        val repository = mockSuccessfulCase()

        repository.getPlaylist()

        verify(service, times(1)).fetchPlaylists()
    }

    @Test
    fun emitMapperPlaylistFromService()  = runBlockingTest {
        val repository = mockSuccessfulCase()

        assertEquals(playlist, repository.getPlaylist().first().getOrNull())
    }

    @Test
    fun propagateErrors() = runBlockingTest {
        val repository = mockFailuerCase()

        assertEquals(exception , repository.getPlaylist().first().exceptionOrNull())
    }

    @Test
    fun delegateBusinessLogicToMapper() = runBlockingTest {
        val repository = mockSuccessfulCase()

        repository.getPlaylist().first()

        verify(mapper, times(1)).invoke(playlistsRaw)
    }

    private suspend fun mockFailuerCase(): PlaylistRepository {
        whenever(service.fetchPlaylists()).thenReturn(
            flow {
                emit(Result.failure<List<PlaylistRaw>>(exception))
            }
        )
        return PlaylistRepository(service, mapper)
    }

    private suspend fun mockSuccessfulCase(): PlaylistRepository {
        whenever(service.fetchPlaylists()).thenReturn(
            flow {
                emit(Result.success(playlistsRaw))
            }
        )

        whenever(mapper.invoke(playlistsRaw)).thenReturn(playlist)
        return PlaylistRepository(service, mapper)
    }
}