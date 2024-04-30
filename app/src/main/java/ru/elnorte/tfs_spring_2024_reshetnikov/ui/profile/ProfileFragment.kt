package ru.elnorte.tfs_spring_2024_reshetnikov.ui.profile

import android.content.Context
import android.view.View
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import ru.elnorte.tfs_spring_2024_reshetnikov.MainApplication
import ru.elnorte.tfs_spring_2024_reshetnikov.R
import ru.elnorte.tfs_spring_2024_reshetnikov.databinding.ProfileFragmentBinding
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.PersonUiModel
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.mvi.BaseFragmentMvi
import ru.elnorte.tfs_spring_2024_reshetnikov.utils.snackbarError
import javax.inject.Inject

class ProfileFragment :
    BaseFragmentMvi<
            ProfilePartialState,
            ProfileIntent,
            ProfileState,
            ProfileEffect
            >(R.layout.profile_fragment) {

    private var fragmentBinding: ProfileFragmentBinding? = null

    @Inject
    lateinit var profileStore: ProfileStore

    override val store: ProfileStore
        get() = profileStore

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (requireActivity().application as MainApplication).appComponent
            .profileComponent().create()
            .inject(this)
    }

    override fun render(state: ProfileState) {
        val binding = ProfileFragmentBinding.bind(requireView())
        fragmentBinding = binding

        when (val dataToRender = state.profileUi) {
            ProfileUiState.Init -> {
                store.postIntent(ProfileIntent.Init)
            }

            is ProfileUiState.Success -> showSuccess(dataToRender, binding)

            is ProfileUiState.Error -> setShimmerHidden()
            ProfileUiState.Loading -> setShimmerVisible()

        }
    }

    private fun showSuccess(
        dataToRender: ProfileUiState.Success<PersonUiModel>,
        binding: ProfileFragmentBinding,
    ) {
        setShimmerHidden()
        val data = dataToRender.data
        with(binding.profileInfoCard) {
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

    override fun resolveEffect(effect: ProfileEffect) {
        when (effect) {
            is ProfileEffect.ShowError -> snackbarError(requireView(), effect.throwableMessage)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentBinding = null
    }

    private fun setShimmerVisible() {
        fragmentBinding?.run {
            profileInfoCard.root.visibility = View.GONE
            profileShimmer.root.visibility = View.VISIBLE
        }
    }

    private fun setShimmerHidden() {
        fragmentBinding?.run {
            profileInfoCard.root.visibility = View.VISIBLE
            profileShimmer.root.visibility = View.GONE
        }
    }
}
