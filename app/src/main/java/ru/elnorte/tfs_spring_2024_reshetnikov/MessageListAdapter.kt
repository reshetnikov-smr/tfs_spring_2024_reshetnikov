package ru.elnorte.tfs_spring_2024_reshetnikov

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.elnorte.tfs_spring_2024_reshetnikov.databinding.MessageItemCardBinding

class MessageListAdapter(private val clickListener: MessageClickListener) :
    ListAdapter<MessageUiModel, MessageListAdapter.ViewHolder>(MessageDiffCallBack()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(private val binding: MessageItemCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MessageUiModel, clickListener: MessageClickListener) {
            binding.message.setAvatar(item.senderAvatar)
            binding.message.hideAvatar(item.isMineMessage)
            binding.message.setName(item.userName)
            binding.message.setMessageText(item.message)
            binding.message.setOnLongClickListener {
                if (item.reactions.isEmpty()) {
                    clickListener.onLongClick(item)
                }
                false
            }
            binding.message.addEmojis(item.reactions, item.checkedReaction)
            binding.message.setOnEmojiClickListener {
                clickListener.onEmojiClick(
                    it,
                    item.messageId
                )
            }
            binding.message.setOnAddReactionClickListener { clickListener.onAddReactionClick(item.messageId) }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = MessageItemCardBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class MessageDiffCallBack : DiffUtil.ItemCallback<MessageUiModel>() {
    override fun areItemsTheSame(oldItem: MessageUiModel, newItem: MessageUiModel): Boolean {
        return oldItem.messageId == newItem.messageId
    }

    override fun areContentsTheSame(oldItem: MessageUiModel, newItem: MessageUiModel): Boolean {
        return oldItem == newItem
    }
}

class MessageClickListener(
    val longClickListener: (messageId: Int) -> Unit,
    val emojiClickListener: (emoji: String, messageId: Int) -> Unit,
    val addReaction: (messageId: Int) -> Unit
) {
    fun onLongClick(item: MessageUiModel): Boolean {
        longClickListener(item.messageId); return true
    }

    fun onEmojiClick(item: String, messageId: Int) {
        emojiClickListener(item, messageId)
    }

    fun onAddReactionClick(messageId: Int) {
        addReaction(messageId)
    }
}
