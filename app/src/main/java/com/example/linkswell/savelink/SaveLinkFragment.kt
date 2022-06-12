package com.example.linkswell.savelink

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.example.linkswell.databinding.FragmentSaveLinkBinding
import com.example.linkswell.di.ViewModelFactory
import com.example.linkswell.home.ui.MainActivity
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SaveLinkFragment : Fragment() {

    private var _binding: FragmentSaveLinkBinding? = null
    private val binding: FragmentSaveLinkBinding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: SaveLinkViewModel by viewModels { viewModelFactory }

    private val args: SaveLinkFragmentArgs by navArgs()

    private val intentChannel = Channel<SaveLinkIntent>(Channel.BUFFERED)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSaveLinkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindVm()
        Log.d("SaveLink", "link: ${args.originalLink}")
        intentChannel.trySend(SaveLinkIntent.OnViewCreated(args.originalLink))
    }

    private fun bindVm() {
        with(viewLifecycleOwner.lifecycleScope) {
            launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.viewState.collect {
                        render(it)
                    }
                }
            }

            launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.sideEffects.collect {
                        handleSideEffects(it)
                    }
                }
            }

            launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    intentChannel.receiveAsFlow()
                        .onEach { viewModel.processIntent(it) }
                        .collect()
                }
            }
        }
    }

    private fun render(viewState: SaveLinkViewState) {
        Log.d("SaveLink", "ui: $viewState")
    }

    private fun handleSideEffects(effect: SaveLinkSideEffect) {
        when(effect) {
            is SaveLinkSideEffect.UpdateLinksFields -> {

            }
            is SaveLinkSideEffect.ShowErrorAndCloseApp -> {
                Toast.makeText(requireContext(), effect.errorMsg, Toast.LENGTH_LONG).show()
                requireActivity().finish()
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}