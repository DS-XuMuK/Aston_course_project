package ru.nikolaykolchin.astonrickandmorty.presentation.episodes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import ru.nikolaykolchin.astonrickandmorty.databinding.FragmentEpisodeFilterDialogBinding
import ru.nikolaykolchin.astonrickandmorty.domain.episodes.EpisodeViewModel

class EpisodeFilterDialog : DialogFragment() {
    private lateinit var binding: FragmentEpisodeFilterDialogBinding
    private val viewModel: EpisodeViewModel by viewModels({ requireParentFragment() })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEpisodeFilterDialogBinding.inflate(inflater, container, false)

        binding.buttonCancel.setOnClickListener {
            dialog?.dismiss()
        }
        binding.buttonOk.setOnClickListener {
            val input = collectData()
            viewModel.receiveDataFromDialog(input)
            dialog?.dismiss()
        }

        return binding.root
    }

    private fun collectData(): MutableList<String?> {
        with(binding) {
            val list = mutableListOf<String?>()

            val name = editTextName.text.toString()
            list.add(if (name == "") null else name)

            val episode = editTextEpisode.text.toString()
            list.add(if (episode == "") null else episode)

            return list
        }
    }

    companion object {
        fun newInstance() = EpisodeFilterDialog()
    }
}