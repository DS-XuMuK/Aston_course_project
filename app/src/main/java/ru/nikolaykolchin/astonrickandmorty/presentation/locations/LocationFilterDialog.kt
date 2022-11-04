package ru.nikolaykolchin.astonrickandmorty.presentation.locations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import ru.nikolaykolchin.astonrickandmorty.databinding.FragmentLocationFilterDialogBinding
import ru.nikolaykolchin.astonrickandmorty.domain.locations.LocationViewModel

class LocationFilterDialog : DialogFragment() {
    private lateinit var binding: FragmentLocationFilterDialogBinding
    private val viewModel: LocationViewModel by viewModels({ requireParentFragment() })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLocationFilterDialogBinding.inflate(inflater, container, false)

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

    private fun collectData() : MutableList<String?> {
        with(binding) {
            val list = mutableListOf<String?>()

            val name = editTextName.text.toString()
            list.add(if (name == "") null else name)

            val type = editTextType.text.toString()
            list.add(if (type == "") null else type)

            val dimension = editTextDimension.text.toString()
            list.add(if (dimension == "") null else dimension)

            return list
        }
    }

    companion object {
        fun newInstance() = LocationFilterDialog()
    }
}