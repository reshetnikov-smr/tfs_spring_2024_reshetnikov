package ru.elnorte.tfs_spring_2024_reshetnikov.ui.person

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import ru.elnorte.tfs_spring_2024_reshetnikov.R
import ru.elnorte.tfs_spring_2024_reshetnikov.databinding.PersonFragmentBinding
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.OnlineStatus

class PersonFragment : Fragment() {
    private var _binding: PersonFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = PersonFragmentBinding.inflate(inflater, container, false)
        setupAndInit()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().navigate(PersonFragmentDirections.actionPersonFragmentToContactsFragment())
        }
    }

    private fun setupAndInit() {
        val arguments = PersonFragmentArgs.fromBundle(requireArguments())
        with(binding) {
            profileInfoCard.profileCardName.text = arguments.name
            profileInfoCard.profileCardStatus.text = arguments.status
            profileInfoCard.profileCardOnline.text = when (arguments.isOnline) {
                OnlineStatus.Online -> "Online"
                OnlineStatus.Idle -> "Idle"
                OnlineStatus.Offline -> "Offline"
            }
            profileInfoCard.profileCardOnline.setTextColor(
                when (arguments.isOnline) {
                    OnlineStatus.Online -> requireContext().getColor(R.color.online)
                    OnlineStatus.Idle -> requireContext().getColor(R.color.idle)
                    OnlineStatus.Offline -> requireContext().getColor(R.color.offline)
                }
            )
            arguments.avatar?.let {
                val imgUri = it.toUri().buildUpon().scheme("https").build()
                Glide.with(profileInfoCard.profileCardAvatar.context)
                    .load(imgUri)
                    .apply(
                        RequestOptions()
                            .placeholder(R.drawable.loading_animation)
                            .error(R.drawable.download_fail_image)
                    )
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(profileInfoCard.profileCardAvatar)
            }
            toolbar.setupWithNavController(findNavController())
            toolbar.setNavigationOnClickListener {
                findNavController().navigate(
                    PersonFragmentDirections.actionPersonFragmentToContactsFragment()
                )
            }
            toolbar.title = ""
        }
    }
}
