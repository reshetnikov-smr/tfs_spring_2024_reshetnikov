package ru.elnorte.tfs_spring_2024_reshetnikov.ui.channels

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.page.PageFragment

class PagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle, fragmentsCount: Int) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    private val fragments = fragmentsCount.createFragments()
    override fun getItemCount() = fragments.size
    override fun createFragment(position: Int) = fragments[position]

    private fun Int.createFragments(): List<PageFragment> {
        return List(this) { index ->
            PageFragment.newInstance(index)
        }
    }
}
