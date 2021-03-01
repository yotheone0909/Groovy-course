package petros.efthymiou.groovy.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_playlist_detail.*
import petros.efthymiou.groovy.R
import javax.inject.Inject

@AndroidEntryPoint
class PlaylistDetailsFragment : Fragment() {

    lateinit var viewModel: PlaylistDetailsViewModel

    @Inject
    lateinit var viewModelFactory : PlaylistDetailsViewModelFactory
    val args: PlaylistDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_playlist_detail, container, false)
        val id = args.playlistId
        setUpViewModel()
        viewModel.getPlaylistDetails(id)

        observerPlaylistDetail()
        observerLoader()
        return view
    }

    private fun observerLoader() {
        viewModel.loader.observe(this as LifecycleOwner, Observer { loading ->
            when (loading) {
                true -> details_loader.visibility = View.VISIBLE
                else -> details_loader.visibility = View.GONE
            }
        })
    }

    private fun observerPlaylistDetail() {
        viewModel.playlistDetails.observe(this as LifecycleOwner, Observer { playlistDetails ->
            if (playlistDetails.getOrNull() != null) {
                setUpUI(playlistDetails)
            } else {
                Snackbar.make(playlist_detail_root,
                R.string.generic_error,
                Snackbar.LENGTH_LONG)
                    .show()
            }
        })
    }

    private fun setUpViewModel() {
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(PlaylistDetailsViewModel::class.java)
    }

    private fun setUpUI(playlistDetails: Result<PlaylistDetails>) {
        playlist_name.text = playlistDetails.getOrNull()!!.name
        playlist_details.text = playlistDetails.getOrNull()!!.details
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PlaylistDetailsFragment()
    }
}