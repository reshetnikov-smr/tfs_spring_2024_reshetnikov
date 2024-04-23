package ru.elnorte.tfs_spring_2024_reshetnikov.ui.page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ru.elnorte.tfs_spring_2024_reshetnikov.MainActivity
import ru.elnorte.tfs_spring_2024_reshetnikov.R
import ru.elnorte.tfs_spring_2024_reshetnikov.data.repository.ChannelRepository
import ru.elnorte.tfs_spring_2024_reshetnikov.data.repository.MessageConverter
import ru.elnorte.tfs_spring_2024_reshetnikov.databinding.PageFragmentBinding
import ru.elnorte.tfs_spring_2024_reshetnikov.isSubscribed
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.channels.ChannelsFragmentDirections
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.TopicTransferModel
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.mvi.BaseFragmentMvi
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.mvi.MviStore
import ru.elnorte.tfs_spring_2024_reshetnikov.utils.ISONLYSUBSCRIBEDARGUMENT
import ru.elnorte.tfs_spring_2024_reshetnikov.utils.QUERY_TEXT_KEY
import ru.elnorte.tfs_spring_2024_reshetnikov.utils.snackbarError

class PageFragment :
    BaseFragmentMvi<
            PagePartialState,
            PageIntent,
            PageState,
            PageEffect
            >(R.layout.page_fragment) {

    private var _binding: PageFragmentBinding? = null
    private val binding get() = _binding!!

    override val store: MviStore<
            PagePartialState,
            PageIntent,
            PageState,
            PageEffect
            > by viewModels {
        PageStoreFactory(
            PageReducer(),
            PageActor(ChannelRepository(MessageConverter()))
        )
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
        newSetup()
        return binding.root
    }

    private fun newSetup() {
        (requireActivity() as MainActivity).showBottomNav()

        adapter = PageListAdapter(PageClickListener({
            store.postIntent(PageIntent.ChannelItemClick(it))
        }, { name, parent ->
            resolveEffect(PageEffect.NavigateToChat(TopicTransferModel(name, parent)))
        }))
        binding.pageRecyclerView.adapter = adapter
        val layoutManager = LinearLayoutManager(requireContext())
        binding.pageRecyclerView.layoutManager = layoutManager
    }

    override fun resolveEffect(effect: PageEffect) {
        when (effect) {
            is PageEffect.NavigateToChat -> {
                this.findNavController()
                    .navigate(
                        ChannelsFragmentDirections.actionToTopic(
                            effect.topicTransferModel.name,
                            effect.topicTransferModel.parent
                        )
                    )
            }

            is PageEffect.ShowError -> snackbarError(requireView(), effect.throwable)
        }
    }

    override fun render(state: PageState) {
        when (val dataToRender = state.pageUi) {
            is PageUiState.Success -> {
                adapter.submitList(dataToRender.data)
                setShimmerHidden()
            }

            is PageUiState.Error -> {}
            PageUiState.Loading -> {
                setShimmerVisible()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getQueryText(bundle: Bundle, key: String) {
        val subscribedFlag = requireArguments().isSubscribed()
        bundle.getString(key)?.let {
            store.postIntent(PageIntent.SendQuery(it, subscribedFlag))
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
