package ru.elnorte.tfs_spring_2024_reshetnikov.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import ru.elnorte.tfs_spring_2024_reshetnikov.R
import ru.elnorte.tfs_spring_2024_reshetnikov.data.repository.UserRepository
import ru.elnorte.tfs_spring_2024_reshetnikov.databinding.ProfileFragmentBinding
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.mvi.BaseFragmentMvi
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.mvi.MviStore
import ru.elnorte.tfs_spring_2024_reshetnikov.utils.snackbarError

class ProfileFragment :
    BaseFragmentMvi<
            ProfilePartialState,
            ProfileIntent,
            ProfileState,
            ProfileEffect
            >(R.layout.profile_fragment) {

    override val store: MviStore<
            ProfilePartialState,
            ProfileIntent,
            ProfileState,
            ProfileEffect
            > by viewModels {
        ProfileStoreFactory(
            ProfileReducer(),
            ProfileActor(UserRepository())
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            store.postIntent(ProfileIntent.Init)
        }
    }

    private var _binding: ProfileFragmentBinding? = null
    private val binding get() = _binding!!

    override fun render(state: ProfileState) {
        when (val dataToRender = state.profileUi) {
            is ProfileUiState.Success -> {
                setShimmerHidden()
                val data = dataToRender.data
                with(binding) {
                    with(profileInfoCard) {
                        data.avatar?.let {
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
                        profileCardName.text = data.name
                        profileCardStatus.text = data.status
                        profileCardOnline.text = data.isOnline.toString()

                    }
                }
            }

            is ProfileUiState.Error -> Unit
            ProfileUiState.Loading -> {
                setShimmerVisible()
            }
        }
    }

    override fun resolveEffect(effect: ProfileEffect) {
        when (effect) {
            is ProfileEffect.ShowError -> snackbarError(requireView(), effect.throwable)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ProfileFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setShimmerVisible() {
        binding.profileInfoCard.root.visibility = View.GONE
        binding.profileShimmer.root.visibility = View.VISIBLE
    }

    private fun setShimmerHidden() {
        binding.profileInfoCard.root.visibility = View.VISIBLE
        binding.profileShimmer.root.visibility = View.GONE
    }
}
