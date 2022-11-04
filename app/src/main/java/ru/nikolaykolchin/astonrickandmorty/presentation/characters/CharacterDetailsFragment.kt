package ru.nikolaykolchin.astonrickandmorty.presentation.characters

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.nikolaykolchin.astonrickandmorty.R
import ru.nikolaykolchin.astonrickandmorty.data.base.BaseFragment
import ru.nikolaykolchin.astonrickandmorty.databinding.FragmentCharacterDetailsBinding
import ru.nikolaykolchin.astonrickandmorty.domain.characters.CharacterDetailsViewModel
import ru.nikolaykolchin.astonrickandmorty.data.episodes.model.EpisodeAdapter
import ru.nikolaykolchin.astonrickandmorty.presentation.locations.LocationDetailsFragment

private const val ARG_PARAM_CHARACTER_ID = "ARG_PARAM_CHARACTER_ID"

class CharacterDetailsFragment : BaseFragment<FragmentCharacterDetailsBinding>() {
    private var characterId: Int = 0
    private val viewModel: CharacterDetailsViewModel by viewModels()
    private val adapter = EpisodeAdapter()
    private var originId = ""
    private var locationId = ""

    override fun createBinding() = FragmentCharacterDetailsBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            characterId = it.getInt(ARG_PARAM_CHARACTER_ID)
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
        viewModel.characterLiveData.observe(viewLifecycleOwner) { item ->
            Picasso.get().load(item.image).placeholder(R.drawable.ic_character_foreground)
                .error(R.drawable.launch_icon).into(binding.image)

            with(binding) {
                name.text = item.name
                status.text = item.status
                species.text = item.species
                type.text = item.type
                gender.text = item.gender
                origin.text = item.origin.name
                location.text = item.location.name
                originId = item.origin.url.substringAfterLast('/')
                locationId = item.location.url.substringAfterLast('/')
                if (originId.isNotBlank()) origin.setTextColor(Color.BLUE)
                if (locationId.isNotBlank()) location.setTextColor(Color.BLUE)
            }
        }

        viewModel.episodeLiveData.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.statusLiveData.observe(viewLifecycleOwner) {
            if (it.isNotBlank()) Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupListeners() {
        binding.origin.setOnClickListener {
            if (originId.isNotBlank()) goToLocation(originId)
        }

        binding.location.setOnClickListener {
            if (locationId.isNotBlank()) goToLocation(locationId)
        }

        binding.swipeElement.setOnRefreshListener {
            processResult()
            binding.swipeElement.isRefreshing = false
        }
    }

    private fun goToLocation(itemId: String) {
        val fragmentDetails = LocationDetailsFragment.newInstance(itemId.toInt())
        val activity = context as AppCompatActivity
        activity.supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragmentDetails)
            .addToBackStack(null)
            .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
            .commit()
    }

    private fun makeRecycler() {
        val recyclerView = binding.recyclerViewEpisodes
        val linearLayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = adapter
        recyclerView.isNestedScrollingEnabled = false
    }

    private fun processResult() {
        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.loadCharacter(characterId)
        }
    }

    companion object {
        fun newInstance(itemId: Int) = CharacterDetailsFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_PARAM_CHARACTER_ID, itemId)
            }
        }
    }
}