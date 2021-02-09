package petros.efthymiou.groovy.playlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_item_list.*
import kotlinx.android.synthetic.main.fragment_item_list.view.*
import okhttp3.OkHttpClient
import petros.efthymiou.groovy.R
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

@AndroidEntryPoint
class PlaylistFragment : Fragment() {

    lateinit var viewModel : PlaylistViewModel

    @Inject
    lateinit var viewModelFactory : PlaylistViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_item_list, container, false)

        setupViewModel()

        viewModel.loader.observe(this as LifecycleOwner, Observer { loading ->
            when(loading) {
                true -> loader.visibility = View.VISIBLE
                else -> loader.visibility = View.GONE
            }
        })

        viewModel.playlist.observe(this as LifecycleOwner, Observer{ playlist ->
            if (playlist.getOrNull() != null) {
                setupList(view.playlists_list, playlist.getOrNull()!!)
            }
        })
        return view
    }

    private fun setupList(
        view: View?,
        playlist: List<Playlist>
    ) {
        with(view as RecyclerView) {
            layoutManager = LinearLayoutManager(context)

            adapter =
                MyPlaylistRecyclerViewAdapter(
                    playlist
                )
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this, viewModelFactory).get(PlaylistViewModel::class.java)
    }

    companion object {

        @JvmStatic
        fun newInstance() =
                PlaylistFragment().apply {}
    }
}