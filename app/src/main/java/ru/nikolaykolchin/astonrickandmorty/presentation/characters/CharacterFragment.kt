package ru.nikolaykolchin.astonrickandmorty.presentation.characters

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
import ru.nikolaykolchin.astonrickandmorty.data.characters.model.CharacterAdapter
import ru.nikolaykolchin.astonrickandmorty.databinding.FragmentCharacterBinding
import ru.nikolaykolchin.astonrickandmorty.domain.characters.CharacterViewModel

class CharacterFragment : BaseFragment<FragmentCharacterBinding>() {
    private val filterDialog = CharacterFilterDialog.newInstance()
    private val adapter = CharacterAdapter()
    private lateinit var recyclerView: RecyclerView
    private lateinit var gridLayoutManager: GridLayoutManager
    private val viewModel: CharacterViewModel by viewModels()

    override fun createBinding() = FragmentCharacterBinding.inflate(layoutInflater)

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
        viewModel.listCharacterLiveData.observe(viewLifecycleOwner) {
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
        recyclerView = binding.recyclerViewCharacters
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

        fun newInstance() = CharacterFragment()
    }
}