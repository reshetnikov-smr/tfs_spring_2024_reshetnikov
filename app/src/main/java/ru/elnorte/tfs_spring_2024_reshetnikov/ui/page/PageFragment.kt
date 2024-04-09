package ru.elnorte.tfs_spring_2024_reshetnikov.ui.page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import ru.elnorte.tfs_spring_2024_reshetnikov.MainActivity
import ru.elnorte.tfs_spring_2024_reshetnikov.data.messengerrepository.FakeRepository
import ru.elnorte.tfs_spring_2024_reshetnikov.databinding.PageFragmentBinding
import ru.elnorte.tfs_spring_2024_reshetnikov.isSubscribed
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.channels.ChannelsFragmentDirections
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.QueryResultUiState
import ru.elnorte.tfs_spring_2024_reshetnikov.utils.ISONLYSUBSCRIBEDARGUMENT
import ru.elnorte.tfs_spring_2024_reshetnikov.utils.QUERY_TEXT_KEY

class PageFragment : Fragment() {

    private var _binding: PageFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PageViewModel by viewModels {
        PageViewModelFactory(FakeRepository(), requireArguments().isSubscribed())
    }

    private lateinit var adapter: PageListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener(
            "fragment ${requireArguments().fragmentIndex()}"
        ) { _, bundle ->
            getQueryText(bundle, QUERY_TEXT_KEY)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = PageFragmentBinding.inflate(inflater, container, false)
        setupAndInit()
        initObservers()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getChannels()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupAndInit() {
        (requireActivity() as MainActivity).showBottomNav()

        adapter = PageListAdapter(PageClickListener({
            viewModel.onChannelClick(it)
        }, {
            viewModel.onTopicClick(it)
        }))
        binding.pageRecyclerView.adapter = adapter
        val layoutManager = LinearLayoutManager(requireContext())
        binding.pageRecyclerView.layoutManager = layoutManager
    }

    private fun initObservers() {
        viewModel.navigateToChat.observe(viewLifecycleOwner) {
            if (it != null) {
                this.findNavController().navigate(ChannelsFragmentDirections.actionToTopic(it))
                viewModel.navigateToChatCompleted()
            }
        }
    }

    private fun getChannels() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    when (uiState) {
                        is QueryResultUiState.Error -> {
                            Snackbar.make(
                                requireView(), uiState.exception.message.orEmpty(),
                                Snackbar.LENGTH_SHORT
                            ).show()
                            setShimmerHidden()
                        }

                        QueryResultUiState.Loading -> {
                            setShimmerVisible()
                        }

                        is QueryResultUiState.Success -> {
                            adapter.submitList(uiState.dataList)
                            setShimmerHidden()
                        }
                    }
                }
            }
        }
    }

    private fun getQueryText(bundle: Bundle, key: String) {
        bundle.getString(key)?.let {
            viewModel.sendQuery(it)
        }
    }

    private fun Bundle.fragmentIndex(): Int = getInt(ISONLYSUBSCRIBEDARGUMENT)
    private fun Bundle.isSubscribed(): Boolean = fragmentIndex().isSubscribed()
    private fun setShimmerVisible() {
        binding.shimmerFragment.root.visibility = View.VISIBLE
        binding.shimmerFragment.root.startShimmer()
        binding.pageRecyclerView.visibility = View.GONE
    }

    private fun setShimmerHidden() {
        binding.shimmerFragment.root.stopShimmer()
        binding.shimmerFragment.root.visibility = View.GONE
        binding.pageRecyclerView.visibility = View.VISIBLE
    }

    companion object {
        fun newInstance(
            position: Int,
        ): PageFragment {
            val args = Bundle().apply {
                putInt(ISONLYSUBSCRIBEDARGUMENT, position)
            }
            return PageFragment().apply {
                arguments = args
            }
        }
    }
}
