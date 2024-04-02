package ru.elnorte.tfs_spring_2024_reshetnikov.ui.contacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import ru.elnorte.tfs_spring_2024_reshetnikov.MainActivity
import ru.elnorte.tfs_spring_2024_reshetnikov.data.messengerrepository.FakeRepository
import ru.elnorte.tfs_spring_2024_reshetnikov.databinding.ContactsFragmentBinding

class ContactsFragment : Fragment() {

    private lateinit var viewModel: ContactsViewModel
    private lateinit var binding: ContactsFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ContactsFragmentBinding.inflate(inflater, container, false)
        setupAndInit()
        return binding.root
    }

    private fun setupAndInit() {
        (requireActivity() as MainActivity).showBottomNav()
        val repo = FakeRepository()
        val viewModelFactory = ContactsViewModelFactory(repo)
        viewModel = ViewModelProvider(this, viewModelFactory)[ContactsViewModel::class.java]
        val adapter = ContactsListAdapter(ContactsClickListener {
            Snackbar.make(requireView(), "user number $it clicked", Snackbar.LENGTH_SHORT).show()
        })
        with(binding) {
            contactsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            contactsRecyclerView.adapter = adapter
        }
        viewModel.peopleModel.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }
}
