package petros.efthymiou.groovy.playlist

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import petros.efthymiou.groovy.playlist.Playlist
import javax.inject.Inject

class PlaylistRepository @Inject constructor(
    private val playlistService: PlaylistService
) {

    suspend fun getPlaylist() : Flow<Result<List<Playlist>>> {
        return playlistService.fetchPlaylists()
    }

}
