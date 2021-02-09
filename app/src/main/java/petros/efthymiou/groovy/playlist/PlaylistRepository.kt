package petros.efthymiou.groovy.playlist

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import petros.efthymiou.groovy.playlist.Playlist
import javax.inject.Inject

class PlaylistRepository @Inject constructor(
    private val playlistService: PlaylistService,
    private val mapper: PlaylistMapper
) {

    suspend fun getPlaylist() : Flow<Result<List<Playlist>>> {
        return playlistService.fetchPlaylists().map {
            if (it.isSuccess)
            Result.success(mapper(it.getOrNull()!!))
            else
                Result.failure(it.exceptionOrNull()!!)
        }
    }

}
