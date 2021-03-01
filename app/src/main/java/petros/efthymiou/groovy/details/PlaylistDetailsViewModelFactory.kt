package petros.efthymiou.groovy.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class PlaylistDetailsViewModelFactory @Inject constructor(
    private val service: PlaylistDetailsService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PlaylistDetailsViewModel(service) as T
    }

}