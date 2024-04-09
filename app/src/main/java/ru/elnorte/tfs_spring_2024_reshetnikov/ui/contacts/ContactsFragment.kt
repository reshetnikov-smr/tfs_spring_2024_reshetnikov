package ru.elnorte.tfs_spring_2024_reshetnikov.ui.contacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import ru.elnorte.tfs_spring_2024_reshetnikov.MainActivity
import ru.elnorte.tfs_spring_2024_reshetnikov.afterTextChanged
import ru.elnorte.tfs_spring_2024_reshetnikov.data.messengerrepository.FakeRepository
import ru.elnorte.tfs_spring_2024_reshetnikov.databinding.ContactsFragmentBinding
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.PersonUiModel
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.QueryResultUiState

class ContactsFragment : Fragment() {

    private val viewModel: ContactsViewModel by viewModels {
        ContactsViewModelFactory(FakeRepository())
    }
    private var _binding: ContactsFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ContactsListAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ContactsFragmentBinding.inflate(inflater, container, false)
        setupAndInit()
        initObservers()
        handleSearchQuery()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupAndInit() {
        (requireActivity() as MainActivity).showBottomNav()

        adapter = ContactsListAdapter(ContactsClickListener {
            Snackbar.make(requireView(), "user number $it clicked", Snackbar.LENGTH_SHORT).show()
        })
        with(binding) {
            contactsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            contactsRecyclerView.adapter = adapter
        }
    }

    private fun initObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState: QueryResultUiState<PersonUiModel> ->
                    when (uiState) {
                        is QueryResultUiState.Success -> {
                            handleContactList(uiState.dataList)
                            setShimmerHidden()
                        }

                        is QueryResultUiState.Error -> {
                            Snackbar.make(
                                requireView(),
                                uiState.exception.message.orEmpty(),
                                Snackbar.LENGTH_SHORT
                            ).show()
                            setShimmerHidden()
                        }

                        QueryResultUiState.Loading -> {
                            setShimmerVisible()
                        }
                    }
                }
            }
        }
    }

    private fun handleContactList(dataList: List<PersonUiModel>) {
        adapter.submitList(dataList)
    }

    private fun handleSearchQuery() {
        binding.userSearch.searchQuery.afterTextChanged { viewModel.sendQueryFlow(it) }
    }

    private fun setShimmerVisible() {
        binding.contactsShimmer.root.visibility = View.VISIBLE
        binding.contactsShimmer.root.startShimmer()
        binding.contactsRecyclerView.visibility = View.GONE
    }

    private fun setShimmerHidden() {
        binding.contactsShimmer.root.stopShimmer()
        binding.contactsShimmer.root.visibility = View.GONE
        binding.contactsRecyclerView.visibility = View.VISIBLE
    }

}
