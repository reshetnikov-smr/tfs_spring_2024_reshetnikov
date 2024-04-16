package ru.elnorte.tfs_spring_2024_reshetnikov.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import ru.elnorte.tfs_spring_2024_reshetnikov.MainActivity
import ru.elnorte.tfs_spring_2024_reshetnikov.R
import ru.elnorte.tfs_spring_2024_reshetnikov.data.repository.UserRepository
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

        val repo = UserRepository()
        val viewModelFactory = ProfileViewModelFactory(repo)
        viewModel = ViewModelProvider(this, viewModelFactory)[ProfileViewModel::class.java]

        viewModel.personModel.observe(viewLifecycleOwner) {
            with(binding) {
                with(profileInfoCard) {
                    it.avatar?.let {
                        val imgUri = it.toUri().buildUpon().scheme("https").build()
                        Glide.with(binding.profileInfoCard.profileCardAvatar.context)
                            .load(imgUri)
                            .apply(
                                RequestOptions()
                                    .placeholder(R.drawable.loading_animation)
                                    .error(R.drawable.download_fail_image)
                            )
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(binding.profileInfoCard.profileCardAvatar)

                    }
                    profileCardAvatar.clipToOutline = true
                    profileCardName.text = it.name
                    profileCardStatus.text = it.status
                    profileCardOnline.text = it.isOnline.toString()

                }
            }
        }
    }
}
