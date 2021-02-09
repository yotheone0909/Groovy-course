package petros.efthymiou.groovy.playlist

import androidx.lifecycle.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import petros.efthymiou.groovy.playlist.Playlist
import petros.efthymiou.groovy.playlist.PlaylistRepository

class PlaylistViewModel(private val playlistRepository: PlaylistRepository) : ViewModel() {

    val loader = MutableLiveData<Boolean>()

    val playlist = liveData<Result<List<Playlist>>> {
        loader.postValue(true)
        emitSource(playlistRepository.getPlaylist()
            .onEach {
                loader.postValue(false)
            }.asLiveData())
    }

}
