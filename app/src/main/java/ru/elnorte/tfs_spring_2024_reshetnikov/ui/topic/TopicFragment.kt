package ru.elnorte.tfs_spring_2024_reshetnikov.ui.topic

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import ru.elnorte.tfs_spring_2024_reshetnikov.MainActivity
import ru.elnorte.tfs_spring_2024_reshetnikov.R
import ru.elnorte.tfs_spring_2024_reshetnikov.data.repository.ChannelRepository
import ru.elnorte.tfs_spring_2024_reshetnikov.data.repository.MessageConverter
import ru.elnorte.tfs_spring_2024_reshetnikov.databinding.ChatFragmentBinding
import ru.elnorte.tfs_spring_2024_reshetnikov.ellog
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.ResultUiState
import ru.elnorte.tfs_spring_2024_reshetnikov.unicodeEmojiToHexString
import ru.elnorte.tfs_spring_2024_reshetnikov.utils.getEmojiNameByUnicode

class TopicFragment : Fragment() {

    private var _binding: ChatFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: TopicViewModel
    private lateinit var itemDecoration: CustomItemDecoration
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = ChatFragmentBinding.inflate(inflater, container, false)
        setupAndInit()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupAndInit() {
        (requireActivity() as MainActivity).hideBottomNav()
        val repo = ChannelRepository(MessageConverter())
        val arguments = TopicFragmentArgs.fromBundle(requireArguments())
        val viewModelFactory = TopicViewModelFactory(arguments.channelId, arguments.topicName, repo)
        viewModel = ViewModelProvider(this, viewModelFactory)[TopicViewModel::class.java]

        val adapter = MessageListAdapter(
            MessageClickListener(
                longClickListener = { messageId ->
                    EmojiEntryDialogFragment {
                        viewModel.addReaction(
                            messageId, getEmojiNameByUnicode(
                                unicodeEmojiToHexString(it)
                            )
                        )
                    }.show(childFragmentManager, "tag")
                },
                emojiClickListener = { emo, mesId, isSelected ->
                    if (isSelected) {
                        ellog("is selected true")
                        viewModel.removeReaction(
                            mesId, getEmojiNameByUnicode(
                                unicodeEmojiToHexString(emo)
                            )
                        )
                    } else {
                        ellog("is selected false")
                        viewModel.addReaction(
                            mesId, getEmojiNameByUnicode(
                                unicodeEmojiToHexString(emo)
                            )
                        )
                    }
                },
                addReaction = { messageId ->
                    EmojiEntryDialogFragment {
                        viewModel.addReaction(
                            messageId, getEmojiNameByUnicode(
                                unicodeEmojiToHexString(it)
                            )
                        )
                    }.show(childFragmentManager, "tag")
                }
            )
        )
        val marginSize = resources.getDimensionPixelSize(R.dimen.margin_humongous)

        itemDecoration = CustomItemDecoration(marginSize)
        binding.chatScreenRecyclerView.addItemDecoration(itemDecoration)

        binding.chatScreenRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.chatScreenRecyclerView.adapter = adapter
        binding.toolbar.setupWithNavController(findNavController())
        binding.toolbar.title = ""
        binding.toolbarTitle.text = arguments.topicName

        binding.chatScreenInputMessageEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int,
            ) {
            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int,
            ) {
            }

            override fun afterTextChanged(s: Editable?) {
                viewModel.onTextChanged(s.toString())
            }
        })

        binding.chatScreenMessageActionImage.setOnClickListener {
            viewModel.onActionButtonClick(binding.chatScreenInputMessageEditText.text.toString())
            binding.chatScreenInputMessageEditText.text.clear()
        }

        viewModel.messageState.observe(viewLifecycleOwner) {
            binding.chatScreenMessageActionImage.setImageDrawable(
                if (it == MessageState.SEND_FILE) {
                    AppCompatResources.getDrawable(
                        requireContext(),
                        R.drawable.add_file_image
                    )
                } else {
                    AppCompatResources.getDrawable(
                        requireContext(),
                        R.drawable.send_message_image
                    )
                }
            )

        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    when (uiState) {
                        is ResultUiState.Error -> {
                            Snackbar
                                .make(requireView(), uiState.errorMessage, Snackbar.LENGTH_LONG)
                                .show()
                        }

                        ResultUiState.Loading -> {}
                        is ResultUiState.Success -> {
                            val oldlistSize = adapter.itemCount
                            adapter.submitList(uiState.dataList)
                            itemDecoration.updateList(uiState.dataList)
                            if (oldlistSize < uiState.dataList.size) {
                                binding.chatScreenRecyclerView.smoothScrollToPosition(adapter.itemCount)
                            }
                        }
                    }
                }
            }
        }

        viewModel.error.observe(viewLifecycleOwner) {
            if (it != null) {
                Snackbar.make(requireView(), it, Snackbar.LENGTH_SHORT).show()
                viewModel.onErrorCompleted()
            }
        }
    }
}
