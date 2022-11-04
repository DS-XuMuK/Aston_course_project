package ru.nikolaykolchin.astonrickandmorty.presentation.episodes

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.nikolaykolchin.astonrickandmorty.data.base.BaseFragment
import ru.nikolaykolchin.astonrickandmorty.data.characters.model.CharacterAdapter
import ru.nikolaykolchin.astonrickandmorty.databinding.FragmentEpisodeDetailsBinding
import ru.nikolaykolchin.astonrickandmorty.domain.episodes.EpisodeDetailsViewModel

private const val ARG_PARAM_EPISODE_ID = "ARG_PARAM_EPISODE_ID"

class EpisodeDetailsFragment : BaseFragment<FragmentEpisodeDetailsBinding>() {
    private var episodeId: Int = 0
    private val viewModel: EpisodeDetailsViewModel by viewModels()
    private val adapter = CharacterAdapter()

    override fun createBinding() = FragmentEpisodeDetailsBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            episodeId = it.getInt(ARG_PARAM_EPISODE_ID)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
        setupListeners()
        makeRecycler()
        processResult()
    }

    private fun setupObservers() {
        viewModel.episodeLiveData.observe(viewLifecycleOwner) { item ->
            with(binding) {
                name.text = item.name
                airDate.text = item.air_date
                episode.text = item.episode
            }
        }

        viewModel.characterLiveData.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.statusLiveData.observe(viewLifecycleOwner) {
            if (it.isNotBlank()) Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupListeners() {
        binding.swipeElement.setOnRefreshListener {
            processResult()
            binding.swipeElement.isRefreshing = false
        }
    }

    private fun makeRecycler() {
        val recyclerView = binding.recyclerViewCharacters
        val layoutManager = GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        recyclerView.isNestedScrollingEnabled = false
    }

    private fun processResult() {
        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.loadEpisode(episodeId)
        }
    }

    companion object {
        fun newInstance(itemId: Int) = EpisodeDetailsFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_PARAM_EPISODE_ID, itemId)
            }
        }
    }
}