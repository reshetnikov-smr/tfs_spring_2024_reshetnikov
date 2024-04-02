package ru.elnorte.tfs_spring_2024_reshetnikov.ui.page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ru.elnorte.tfs_spring_2024_reshetnikov.MainActivity
import ru.elnorte.tfs_spring_2024_reshetnikov.data.messengerrepository.FakeRepository
import ru.elnorte.tfs_spring_2024_reshetnikov.databinding.PageFragmentBinding
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.channels.ChannelsFragmentDirections
import ru.elnorte.tfs_spring_2024_reshetnikov.utils.ISONLYSUBSCRIBEDARGUMENT

class PageFragment : Fragment() {

    private var isSubscribedOnly = false
    private lateinit var viewModel: PageViewModel
    private lateinit var binding: PageFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = PageFragmentBinding.inflate(inflater, container, false)
        setupAndInit()
        return binding.root
    }

    private fun setupAndInit() {
        (requireActivity() as MainActivity).showBottomNav()
        val repo = FakeRepository()

        if (arguments?.getBoolean(ISONLYSUBSCRIBEDARGUMENT) == true) {
            isSubscribedOnly = true
        }
        val viewModelFactory = PageViewModelFactory(repo, isSubscribedOnly)
        viewModel = ViewModelProvider(this, viewModelFactory)[PageViewModel::class.java]

        val adapter = PageListAdapter(PageClickListener({
            viewModel.onChannelClick(it)
        }, {
            viewModel.onTopicClick(it)
        }))
        binding.pageRecyclerView.adapter = adapter
        val layoutManager = LinearLayoutManager(requireContext())
        binding.pageRecyclerView.layoutManager = layoutManager


        viewModel.model.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        viewModel.navigateToChat.observe(viewLifecycleOwner) {
            if (it != null) {
                this.findNavController().navigate(ChannelsFragmentDirections.actionToTopic(it))
                viewModel.navigateToChatCompleted()
            }
        }
    }
}
