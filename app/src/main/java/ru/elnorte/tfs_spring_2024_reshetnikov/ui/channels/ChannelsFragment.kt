package ru.elnorte.tfs_spring_2024_reshetnikov.ui.channels

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import ru.elnorte.tfs_spring_2024_reshetnikov.R
import ru.elnorte.tfs_spring_2024_reshetnikov.databinding.ChannelsFragmentBinding
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.page.PageFragment
import ru.elnorte.tfs_spring_2024_reshetnikov.utils.ISONLYSUBSCRIBEDARGUMENT

class ChannelsFragment : Fragment() {

    private lateinit var binding: ChannelsFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = ChannelsFragmentBinding.inflate(inflater, container, false)
        setupAndInit()
        return binding.root
    }

    private fun setupAndInit() {
        val tabs: List<String> = listOf(
            getString(R.string.subscribed_tab),
            getString(R.string.all_streams_tab),
        )
        val pagerAdapter = PagerAdapter(childFragmentManager, lifecycle)
        binding.viewPager.adapter = pagerAdapter
        pagerAdapter.update(
            listOf(PageFragment().apply {
                arguments = Bundle().apply { putBoolean(ISONLYSUBSCRIBEDARGUMENT, false) }
            }, PageFragment().apply {
                arguments = Bundle().apply { putBoolean(ISONLYSUBSCRIBEDARGUMENT, true) }
            })
        )
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = tabs[position]
        }.attach()
    }
}
