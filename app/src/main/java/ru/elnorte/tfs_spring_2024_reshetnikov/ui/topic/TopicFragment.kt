package ru.elnorte.tfs_spring_2024_reshetnikov.ui.topic

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import ru.elnorte.tfs_spring_2024_reshetnikov.MainActivity
import ru.elnorte.tfs_spring_2024_reshetnikov.R
import ru.elnorte.tfs_spring_2024_reshetnikov.data.messengerrepository.FakeRepository
import ru.elnorte.tfs_spring_2024_reshetnikov.databinding.ChatFragmentBinding

class TopicFragment : Fragment() {
    private lateinit var binding: ChatFragmentBinding
    private lateinit var viewModel: TopicViewModel
    private lateinit var itemDecoration: CustomItemDecoration
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ChatFragmentBinding.inflate(inflater, container, false)
        setupAndInit()
        return binding.root
    }

    private fun setupAndInit() {
        (requireActivity() as MainActivity).hideBottomNav()
        val repo = FakeRepository()
        val arguments = TopicFragmentArgs.fromBundle(requireArguments())
        val viewModelFactory = TopicViewModelFactory(arguments.topicId, repo)
        viewModel = ViewModelProvider(this, viewModelFactory)[TopicViewModel::class.java]

        val adapter = MessageListAdapter(
            MessageClickListener(
                longClickListener = { messageId ->
                    EmojiEntryDialogFragment {
                        viewModel.toggleReaction(messageId, it)
                    }.show(childFragmentManager, "tag")
                },
                emojiClickListener = { emo, mid -> viewModel.toggleReaction(mid, emo) },
                addReaction = { messageId ->
                    EmojiEntryDialogFragment {
                        viewModel.toggleReaction(messageId, it)
                    }.show(childFragmentManager, "tag")
                }
            )
        )
        val marginSize = resources.getDimensionPixelSize(R.dimen.margin_humongous)

        itemDecoration = CustomItemDecoration(marginSize)
        binding.chatScreenRecyclerView.addItemDecoration(itemDecoration)

        binding.chatScreenRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.chatScreenRecyclerView.adapter = adapter


        binding.chatScreenInputMessageEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
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
        viewModel.messagesList.observe(viewLifecycleOwner) {
            val oldlistSize = adapter.itemCount
            adapter.submitList(it)
            itemDecoration.updateList(it)
            if (oldlistSize < it.size) {
                binding.chatScreenRecyclerView.smoothScrollToPosition(adapter.itemCount)
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
