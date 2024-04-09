package ru.elnorte.tfs_spring_2024_reshetnikov.ui.channels

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.material.tabs.TabLayoutMediator
import ru.elnorte.tfs_spring_2024_reshetnikov.R
import ru.elnorte.tfs_spring_2024_reshetnikov.afterTextChanged
import ru.elnorte.tfs_spring_2024_reshetnikov.databinding.ChannelsFragmentBinding
import ru.elnorte.tfs_spring_2024_reshetnikov.utils.QUERY_TEXT_KEY


class ChannelsFragment : Fragment() {

    private var _binding: ChannelsFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ChannelsViewModel by viewModels()

    private lateinit var pagerAdapter: PagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        _binding = ChannelsFragmentBinding.inflate(inflater, container, false)
        setupAndInit()
        initObservers()
        handleSearchQuery()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun handleSearchQuery() {
        binding.searchQuery.afterTextChanged {
            viewModel.sendQueryFlow(it)
        }
    }

    private fun sendInputQuery(text: String, position: Int) {
        childFragmentManager.run {
            val args = bundleOf(
                QUERY_TEXT_KEY to text
            )
            setFragmentResult(
                "fragment $position", args
            )
        }
    }

    private fun initObservers() {
        viewModel.queryText.observe(viewLifecycleOwner) {
            sendInputQuery(it.text, it.position)
        }
    }

    private fun setupAndInit() {
        val tabs: List<String> = listOf(
            getString(R.string.subscribed_tab),
            getString(R.string.all_streams_tab),
        )
        pagerAdapter = PagerAdapter(childFragmentManager, lifecycle, tabs.size)
        binding.viewPager.adapter = pagerAdapter
        binding.viewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                viewModel.changePosition(position)
            }
        })
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = tabs[position]
        }.attach()
    }
}
