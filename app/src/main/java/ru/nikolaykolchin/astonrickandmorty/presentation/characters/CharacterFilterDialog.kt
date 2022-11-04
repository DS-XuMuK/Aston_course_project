package ru.nikolaykolchin.astonrickandmorty.presentation.characters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import ru.nikolaykolchin.astonrickandmorty.domain.characters.CharacterViewModel
import ru.nikolaykolchin.astonrickandmorty.databinding.FragmentCharacterFilterDialogBinding

class CharacterFilterDialog : DialogFragment() {
    private var _binding: FragmentCharacterFilterDialogBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CharacterViewModel by viewModels({requireParentFragment()})

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCharacterFilterDialogBinding.inflate(inflater, container, false)

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

            val status =
                radioGroupStatus.findViewById<RadioButton>(radioGroupStatus.checkedRadioButtonId).text.toString()
            list.add(if (status == "any") null else status)

            val species = editTextSpecies.text.toString()
            list.add(if (species == "") null else species)

            val type = editTextType.text.toString()
            list.add(if (type == "") null else type)

            val gender =
                radioGroupGender.findViewById<RadioButton>(radioGroupGender.checkedRadioButtonId).text.toString()
            list.add(if (gender == "any") null else gender)

            return list
        }
    }

    companion object {
        fun newInstance() = CharacterFilterDialog()
    }
}