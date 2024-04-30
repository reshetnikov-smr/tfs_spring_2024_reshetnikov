package ru.elnorte.tfs_spring_2024_reshetnikov.ui.page

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ru.elnorte.tfs_spring_2024_reshetnikov.MainActivity
import ru.elnorte.tfs_spring_2024_reshetnikov.MainApplication
import ru.elnorte.tfs_spring_2024_reshetnikov.R
import ru.elnorte.tfs_spring_2024_reshetnikov.databinding.PageFragmentBinding
import ru.elnorte.tfs_spring_2024_reshetnikov.isSubscribed
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.channels.ChannelsFragmentDirections
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.TopicTransferModel
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.mvi.BaseFragmentMvi
import ru.elnorte.tfs_spring_2024_reshetnikov.utils.ISONLYSUBSCRIBEDARGUMENT
import ru.elnorte.tfs_spring_2024_reshetnikov.utils.QUERY_TEXT_KEY
import ru.elnorte.tfs_spring_2024_reshetnikov.utils.snackbarError
import javax.inject.Inject

class PageFragment :
    BaseFragmentMvi<
            PagePartialState,
            PageIntent,
            PageState,
            PageEffect
            >(R.layout.page_fragment) {

    private var fragmentBinding: PageFragmentBinding? = null

    @Inject
    lateinit var pageStore: PageStore

    override val store: PageStore
        get() = pageStore

    private lateinit var adapter: PageListAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (requireActivity().application as MainApplication).appComponent
            .channelComponent().create()
            .injectPageFragment(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val binding = PageFragmentBinding.inflate(inflater, container, false)
        fragmentBinding = binding
        newSetup(binding)
        return binding.root
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener(
            "fragment ${requireArguments().fragmentIndex()}"
        ) { _, bundle ->
            getQueryText(bundle, QUERY_TEXT_KEY)
        }
    }

    private fun newSetup(binding: PageFragmentBinding) {
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

            is PageEffect.ShowError -> snackbarError(
                requireView(),
                effect.throwable.message.orEmpty()
            )
        }
    }

    override fun render(state: PageState) {
        when (state) {
            PageInit -> {
                store.postIntent(PageIntent.Init(requireArguments().isSubscribed()))
            }

            PageLoading -> {
                setShimmerVisible()
            }

            is PageSuccess -> {
                adapter.submitList(state.list)
                setShimmerHidden()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentBinding = null
    }

    private fun getQueryText(bundle: Bundle, key: String) {
        val iSubscribed = requireArguments().isSubscribed()
        bundle.getString(key)?.let {
            store.postIntent(PageIntent.SendQuery(it, iSubscribed))
        }
    }

    private fun Bundle.fragmentIndex(): Int = getInt(ISONLYSUBSCRIBEDARGUMENT)
    private fun Bundle.isSubscribed(): Boolean = fragmentIndex().isSubscribed()
    private fun setShimmerVisible() {
        fragmentBinding?.run {
            shimmerFragment.root.visibility = View.VISIBLE
            shimmerFragment.root.startShimmer()
            pageRecyclerView.visibility = View.GONE
        }
    }

    private fun setShimmerHidden() {
        fragmentBinding?.run {
            shimmerFragment.root.stopShimmer()
            shimmerFragment.root.visibility = View.GONE
            pageRecyclerView.visibility = View.VISIBLE
        }
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
