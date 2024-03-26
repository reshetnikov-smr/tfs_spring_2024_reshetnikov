package ru.elnorte.tfs_spring_2024_reshetnikov

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar

import ru.elnorte.tfs_spring_2024_reshetnikov.databinding.ChatScreenLayoutBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ChatScreenLayoutBinding
    private var messageState = MessageState.SEND_FILE
    private lateinit var repo: MockRepository
    private lateinit var adapter: MessageListAdapter
    private lateinit var itemDecoration: CustomItemDecoration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViews()
        setContentView(binding.root)
    }

    private fun initViews() {
        repo = MockRepository(this)
        binding = ChatScreenLayoutBinding.inflate(layoutInflater)
        binding.chatScreenInputMessageEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                messageState = if (s?.isNotEmpty() == true) {
                    binding.chatScreenMessageActionImage.setImageDrawable(
                        AppCompatResources.getDrawable(
                            this@MainActivity,
                            R.drawable.send_message_image
                        )
                    )
                    MessageState.SEND_MESSAGE
                } else {
                    binding.chatScreenMessageActionImage.setImageDrawable(
                        AppCompatResources.getDrawable(
                            this@MainActivity,
                            R.drawable.add_file_image
                        )
                    )
                    MessageState.SEND_FILE
                }
            }
        })
        adapter = MessageListAdapter(
            MessageClickListener({ messageId ->
                EmojiEntryDialogFragment {
                    repo.toggleReaction(messageId, it)
                    update()
                }.show(supportFragmentManager, "tag")
            }, { emo, mid -> repo.toggleReaction(mid, emo); update() }, { messageId ->
                EmojiEntryDialogFragment {
                    repo.toggleReaction(messageId, it)
                    update()
                }.show(supportFragmentManager, "tag")
            })
        )
        val marginSize = resources.getDimensionPixelSize(R.dimen.margin_humongous)
        itemDecoration =
            CustomItemDecoration(marginSize).apply { updateList(repo.getConversation()) }
        binding.chatScreenRecyclerView.addItemDecoration(itemDecoration)
        binding.chatScreenRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.chatScreenRecyclerView.adapter = adapter
        adapter.submitList(repo.getConversation())
        binding.chatScreenMessageActionImage.setOnClickListener {
            if (messageState == MessageState.SEND_MESSAGE) {
                repo.sendMessage(binding.chatScreenInputMessageEditText.text.toString())
                //will be replaced once we allowed to use viewmodels and livedata
                val lastPosition = repo.getConversation().size
                update()
                binding.chatScreenRecyclerView.smoothScrollToPosition(lastPosition - 1)
                binding.chatScreenInputMessageEditText.text.clear()
            } else {
                Snackbar.make(binding.root, getString(R.string.wip_stub), Snackbar.LENGTH_SHORT)
                    .show()
            }

        }
    }

    private fun update() {
        itemDecoration.updateList(repo.getConversation())
        adapter.submitList(repo.getConversation())
    }
}

enum class MessageState {
    SEND_FILE, SEND_MESSAGE
}
