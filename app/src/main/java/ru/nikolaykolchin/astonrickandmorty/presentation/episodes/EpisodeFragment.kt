package ru.nikolaykolchin.astonrickandmorty.presentation.episodes

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.nikolaykolchin.astonrickandmorty.data.Constants.DIALOG_TAG
import ru.nikolaykolchin.astonrickandmorty.data.base.BaseFragment
import ru.nikolaykolchin.astonrickandmorty.databinding.FragmentEpisodeBinding
import ru.nikolaykolchin.astonrickandmorty.data.episodes.model.EpisodeAdapter
import ru.nikolaykolchin.astonrickandmorty.domain.episodes.EpisodeViewModel

class EpisodeFragment : BaseFragment<FragmentEpisodeBinding>() {
    private val filterDialog = EpisodeFilterDialog.newInstance()
    private val adapter = EpisodeAdapter()
    private lateinit var recyclerView: RecyclerView
    private lateinit var gridLayoutManager: GridLayoutManager
    private val viewModel: EpisodeViewModel by viewModels()

    override fun createBinding() = FragmentEpisodeBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
        setupRecycleView()
        setupListeners()

        if (!isAlreadyLoaded) processResult()
        isAlreadyLoaded = true
    }

    private fun setupListeners() {
        binding.filterButton.setOnClickListener {
            filterDialog.show(childFragmentManager, DIALOG_TAG)
        }

        binding.swipeElement.setOnRefreshListener {
            setupRecycleView()
            processResult()
            binding.swipeElement.isRefreshing = false
        }

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (gridLayoutManager.findLastCompletelyVisibleItemPosition() == adapter.itemCount - 1) {
                    binding.progressBar.visibility = View.VISIBLE

                    processResult()
                }
            }
        })
    }

    private fun setupObservers() {
        viewModel.listEpisodeLiveData.observe(viewLifecycleOwner) {
            if (it.size > 0) adapter.submitList(it)
        }
        viewModel.loadingStatusLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.visibility = View.GONE
            if (it.isNotBlank()) Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
        viewModel.dialogChangeLiveData.observe(viewLifecycleOwner) {
            if (it) {
                adapter.submitList(emptyList())
                setupRecycleView()
                processResult()
            }
        }
    }

    private fun setupRecycleView() {
        recyclerView = binding.recyclerViewEpisodes
        gridLayoutManager =
            GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.adapter = adapter
    }

    private fun processResult() {
        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.loadData()
        }
    }

    companion object {
        var isAlreadyLoaded = false

        fun newInstance() = EpisodeFragment()
    }
}