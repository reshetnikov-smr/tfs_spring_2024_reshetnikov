package ru.elnorte.tfs_spring_2024_reshetnikov.ui.topic

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.elnorte.tfs_spring_2024_reshetnikov.MainActivity
import ru.elnorte.tfs_spring_2024_reshetnikov.MainApplication
import ru.elnorte.tfs_spring_2024_reshetnikov.R
import ru.elnorte.tfs_spring_2024_reshetnikov.afterTextChanged
import ru.elnorte.tfs_spring_2024_reshetnikov.databinding.ChatFragmentBinding
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.MessageUiModel
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.mvi.BaseFragmentMvi
import ru.elnorte.tfs_spring_2024_reshetnikov.unicodeEmojiToHexString
import ru.elnorte.tfs_spring_2024_reshetnikov.utils.getEmojiNameByUnicode
import ru.elnorte.tfs_spring_2024_reshetnikov.utils.snackbarError
import javax.inject.Inject

class TopicFragment : BaseFragmentMvi<
        TopicPartialState,
        TopicIntent,
        TopicState,
        TopicEffect
        >(R.layout.chat_fragment), EmojiCallback {

    private var fragmentBinding: ChatFragmentBinding? = null

    @Inject
    lateinit var topicStore: TopicStore

    override val store: TopicStore
        get() = topicStore

    private lateinit var adapter: MessageListAdapter
    private lateinit var itemDecoration: CustomItemDecoration

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (requireActivity().application as MainApplication).appComponent
            .chatComponent().create()
            .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fetchData()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = ChatFragmentBinding.inflate(inflater, container, false)
        fragmentBinding = binding
        newSetup()
        return binding.root
    }

    override fun render(state: TopicState) {
        when (state) {
            is TopicSuccess -> {
                val data = state.data
                fragmentBinding?.manageSendButton(state.data.messageState)
                updateList(data.messages, state.showLastMessage)
            }

            else -> {}
        }
    }

    override fun resolveEffect(effect: TopicEffect) {
        when (effect) {
            is TopicEffect.ShowEmojiDialog -> {
                EmojiEntryDialogFragment.newInstance(effect.messageId)
                    .show(childFragmentManager, "TopicFragment")
            }

            is TopicEffect.ShowError -> snackbarError(
                requireView(),
                effect.throwableMessage
            )

            TopicEffect.DownToList -> {
                fragmentBinding?.chatScreenRecyclerView?.smoothScrollToPosition(
                    adapter.itemCount - 1
                )
            }
        }
    }

    override fun onSelectEmoji(emoji: String, messageId: Int) {
        store.postIntent(
            TopicIntent.AddReaction(
                messageId, getEmojiNameByUnicode(
                    unicodeEmojiToHexString(emoji)
                )
            )
        )
    }

    private fun ChatFragmentBinding.manageSendButton(messageState: MessageState) {
        chatScreenMessageActionImage.setImageDrawable(
            if (messageState == MessageState.SEND_FILE) {
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

    private fun updateList(
        messages: List<MessageUiModel>,
        showLastMessage: Boolean,
    ) {
        adapter.submitList(messages) {
            if (showLastMessage) {
                resolveEffect(TopicEffect.DownToList)
            }
        }
        itemDecoration.updateList(messages)
    }

    override fun onDestroyView() {
        fragmentBinding = null
        super.onDestroyView()
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
                        store.postIntent(
                            TopicIntent.RemoveReaction(
                                mesId, getEmojiNameByUnicode(
                                    unicodeEmojiToHexString(emo)
                                )
                            )
                        )
                    } else {
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

        itemDecoration = CustomItemDecoration()
        fragmentBinding?.run {
            chatScreenRecyclerView.addItemDecoration(itemDecoration)

            chatScreenRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            chatScreenRecyclerView.adapter = adapter

            toolbar.setupWithNavController(findNavController())
            toolbar.title = ""
            toolbarTitle.text = arguments.topicName
            chatScreenMessageActionImage.setOnClickListener {
                store.postIntent(
                    TopicIntent.ActionButtonClickIntent(
                        chatScreenInputMessageEditText.text.toString()
                    )
                )
                chatScreenInputMessageEditText.text.clear()
            }

            chatScreenInputMessageEditText.afterTextChanged {
                store.postIntent(TopicIntent.TextEnteredIntent(it.isEmpty()))
            }
        }
    }

    private fun fetchData() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                while (true) {
                    delay(3000)
                    store.postIntent(TopicIntent.ReloadPage)
                }
            }
        }
    }
}
