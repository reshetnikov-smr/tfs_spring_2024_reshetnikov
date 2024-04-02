package ru.elnorte.tfs_spring_2024_reshetnikov.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.elnorte.tfs_spring_2024_reshetnikov.MainActivity
import ru.elnorte.tfs_spring_2024_reshetnikov.data.messengerrepository.FakeRepository
import ru.elnorte.tfs_spring_2024_reshetnikov.databinding.ProfileFragmentBinding

class ProfileFragment : Fragment() {

    private lateinit var viewModel: ProfileViewModel
    private lateinit var binding: ProfileFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = ProfileFragmentBinding.inflate(inflater, container, false)
        setupAndInit()
        return binding.root
    }

    private fun setupAndInit() {
        (requireActivity() as MainActivity).showBottomNav()

        val repo = FakeRepository()
        val viewModelFactory = ProfileViewModelFactory(repo)
        viewModel = ViewModelProvider(this, viewModelFactory)[ProfileViewModel::class.java]

        viewModel.personModel.observe(viewLifecycleOwner) {
            with(binding) {
                with(profileInfoCard) {
                    profileCardAvatar.setImageDrawable(
                        it.avatar?.let {
                            AppCompatResources.getDrawable(
                                requireContext(),
                                it
                            )
                        }
                    )
                    profileCardAvatar.clipToOutline = true
                    profileCardName.text = it.name
                    profileCardStatus.text = it.status
                    profileCardOnline.text = it.isOnline

                }
            }
        }
    }
}
