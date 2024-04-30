package ru.elnorte.tfs_spring_2024_reshetnikov.ui.contacts

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ru.elnorte.tfs_spring_2024_reshetnikov.MainApplication
import ru.elnorte.tfs_spring_2024_reshetnikov.R
import ru.elnorte.tfs_spring_2024_reshetnikov.afterTextChanged
import ru.elnorte.tfs_spring_2024_reshetnikov.databinding.ContactsFragmentBinding
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.PersonUiModel
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.mvi.BaseFragmentMvi
import ru.elnorte.tfs_spring_2024_reshetnikov.utils.snackbarError
import javax.inject.Inject

class ContactsFragment :
    BaseFragmentMvi<ContactsPartialState, ContactsIntent, ContactsState, ContactsEffect>(
        R.layout.contacts_fragment
    ) {

    @Inject
    lateinit var contactStore: ContactsStore

    override val store
        get() = contactStore

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (requireActivity().application as MainApplication).appComponent
            .contactComponent().create()
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            store.postIntent(ContactsIntent.Init)
        }
    }

    private var _binding: ContactsFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ContactsListAdapter

    override fun resolveEffect(effect: ContactsEffect) {
        when (effect) {
            is ContactsEffect.ShowError -> snackbarError(
                requireView(),
                effect.message
            )

            is ContactsEffect.NavigateToPerson -> this.findNavController().navigate(
                ContactsFragmentDirections.actionToPerson(
                    effect.avatar,
                    effect.name,
                    effect.status,
                    effect.isOnline
                )
            )
        }
    }

    override fun render(state: ContactsState) {
        when (val dataToRender = state.contactsUi) {
            is ContactsUiState.Success -> {
                val data = dataToRender.data
                handleContactList(data)
                setShimmerHidden()
            }

            is ContactsUiState.Error -> {

            }

            ContactsUiState.Loading -> {
                setShimmerVisible()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = ContactsFragmentBinding.inflate(inflater, container, false)
        setupAndInit()
        handleSearchQuery()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupAndInit() {
        adapter = ContactsListAdapter(
            ContactsClickListener {
                resolveEffect(
                    ContactsEffect.NavigateToPerson(
                        it.avatar,
                        it.name,
                        it.status,
                        it.isOnline
                    )
                )
            }
        )

        with(binding) {
            contactsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            contactsRecyclerView.adapter = adapter
        }
    }

    private fun handleContactList(dataList: List<PersonUiModel>) {
        adapter.submitList(dataList)
    }

    private fun handleSearchQuery() {
        binding.userSearch.searchQuery.afterTextChanged {
            queryHandle(it)
        }
    }

    private fun queryHandle(query: String) {
        (store as? ContactsStore)?.updateQuery(query)
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
