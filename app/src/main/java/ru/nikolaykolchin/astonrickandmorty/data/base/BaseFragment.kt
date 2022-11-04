package ru.nikolaykolchin.astonrickandmorty.data.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import ru.nikolaykolchin.astonrickandmorty.data.Constants.BINDING_NOT_INIT

abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    private var _binding: VB? = null
    val binding get() =  requireNotNull(_binding) { BINDING_NOT_INIT }

    @CallSuper
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = createBinding()
        return binding.root
    }

    abstract fun createBinding(): VB
}