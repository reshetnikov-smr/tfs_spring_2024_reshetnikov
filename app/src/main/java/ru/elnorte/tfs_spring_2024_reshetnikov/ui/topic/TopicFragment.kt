package ru.elnorte.tfs_spring_2024_reshetnikov.ui.topic

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ru.elnorte.tfs_spring_2024_reshetnikov.MainActivity
import ru.elnorte.tfs_spring_2024_reshetnikov.R
import ru.elnorte.tfs_spring_2024_reshetnikov.data.repository.ChannelRepository
import ru.elnorte.tfs_spring_2024_reshetnikov.data.repository.MessageConverter
import ru.elnorte.tfs_spring_2024_reshetnikov.databinding.ChatFragmentBinding
import ru.elnorte.tfs_spring_2024_reshetnikov.ellog
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.MessageUiModel
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.mvi.BaseFragmentMvi
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.mvi.MviStore
import ru.elnorte.tfs_spring_2024_reshetnikov.unicodeEmojiToHexString
import ru.elnorte.tfs_spring_2024_reshetnikov.utils.getEmojiNameByUnicode
import ru.elnorte.tfs_spring_2024_reshetnikov.utils.snackbarError

class TopicFragment : BaseFragmentMvi<
        TopicPartialState,
        TopicIntent,
        TopicState,
        TopicEffect
        >(R.layout.chat_fragment) {

    private var _binding: ChatFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: MessageListAdapter

    override val store: MviStore<
            TopicPartialState,
            TopicIntent,
            TopicState,
            TopicEffect
            > by viewModels {
        TopicStoreFactory(
            TopicReducer(),
            TopicActor(ChannelRepository(MessageConverter()))
        )
    }
    private lateinit var itemDecoration: CustomItemDecoration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            store.postIntent(TopicIntent.NavigateBack)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = ChatFragmentBinding.inflate(inflater, container, false)
        newSetup()

        return binding.root
    }

    override fun resolveEffect(effect: TopicEffect) {
        when (effect) {
            is TopicEffect.ShowEmojiDialog -> {
                EmojiEntryDialogFragment {
                    store.postIntent(
                        TopicIntent.AddReaction(
                            effect.messageId, getEmojiNameByUnicode(
                                unicodeEmojiToHexString(it)
                            )
                        )
                    )
                }.show(childFragmentManager, "tag")
            }

            is TopicEffect.ShowError -> snackbarError(requireView(), effect.throwable)
            TopicEffect.NavigateBack -> findNavController().navigate(TopicFragmentDirections.actionTopicFragmentToChannelsFragment())
        }
    }

    override fun render(state: TopicState) {
        when (val dataToRender = state.topicUi) {
            is TopicUiState.Success -> {
                val data = dataToRender.data
                binding.chatScreenMessageActionImage.setImageDrawable(
                    if (data.messageState == MessageState.SEND_FILE) {
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
                updateList(data.messages)
            }

            is TopicUiState.Error -> {
            }

            TopicUiState.Loading -> {
            }
        }
    }

    private fun updateList(messages: List<MessageUiModel>) {
        adapter.submitList(messages)
        itemDecoration.updateList(messages)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun newSetup() {
        (requireActivity() as MainActivity).hideBottomNav()
        val arguments = TopicFragmentArgs.fromBundle(requireArguments())
        store.postIntent(TopicIntent.Init(arguments.channelId, arguments.topicName))
        adapter = MessageListAdapter(
            MessageClickListener(
                longClickListener = { messageId ->
                    store.postIntent(TopicIntent.LongMessageClick(messageId))
                },
                emojiClickListener = { emo, mesId, isSelected ->
                    if (isSelected) {
                        ellog("is selected true")
                        store.postIntent(
                            TopicIntent.RemoveReaction(
                                mesId, getEmojiNameByUnicode(
                                    unicodeEmojiToHexString(emo)
                                )
                            )
                        )
                    } else {
                        ellog("is selected false")
                        store.postIntent(
                            TopicIntent.AddReaction(
                                mesId, getEmojiNameByUnicode(
                                    unicodeEmojiToHexString(emo)
                                )
                            )
                        )
                    }
                },
                addReaction = { messageId ->
                    store.postIntent(TopicIntent.LongMessageClick(messageId))
                },
            )
        )
        val marginSize = resources.getDimensionPixelSize(R.dimen.margin_humongous)

        itemDecoration = CustomItemDecoration(marginSize)
        binding.chatScreenRecyclerView.addItemDecoration(itemDecoration)

        binding.chatScreenRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.chatScreenRecyclerView.adapter = adapter

        binding.toolbar.setupWithNavController(findNavController())
        binding.toolbar.setNavigationOnClickListener { store.postIntent(TopicIntent.NavigateBack) }
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
                if (s.toString() == "") {
                    store.postIntent(TopicIntent.TextEnteredIntent(isEmpty = true))
                } else {
                    store.postIntent(TopicIntent.TextEnteredIntent(isEmpty = false))
                }

            }
        })

        binding.chatScreenMessageActionImage.setOnClickListener {
            store.postIntent(TopicIntent.ActionButtonClickIntent(binding.chatScreenInputMessageEditText.text.toString()))
            binding.chatScreenInputMessageEditText.text.clear()
        }
    }
}
