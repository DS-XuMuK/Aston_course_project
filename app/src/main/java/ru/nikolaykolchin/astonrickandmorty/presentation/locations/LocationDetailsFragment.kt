package ru.nikolaykolchin.astonrickandmorty.presentation.locations

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
import ru.nikolaykolchin.astonrickandmorty.databinding.FragmentLocationDetailsBinding
import ru.nikolaykolchin.astonrickandmorty.domain.locations.LocationDetailsViewModel

private const val ARG_PARAM_LOCATION_ID = "ARG_PARAM_LOCATION_ID"

class LocationDetailsFragment : BaseFragment<FragmentLocationDetailsBinding>() {
    private var locationId: Int = 0
    private val viewModel: LocationDetailsViewModel by viewModels()
    private val adapter = CharacterAdapter()

    override fun createBinding() = FragmentLocationDetailsBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            locationId = it.getInt(ARG_PARAM_LOCATION_ID)
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
        viewModel.locationLiveData.observe(viewLifecycleOwner) {item ->
            with(binding) {
                name.text = item.name
                type.text = item.type
                dimension.text = item.dimension
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
            viewModel.loadLocation(locationId)
        }
    }

    companion object {
        fun newInstance(itemId: Int) = LocationDetailsFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_PARAM_LOCATION_ID, itemId)
            }
        }
    }
}