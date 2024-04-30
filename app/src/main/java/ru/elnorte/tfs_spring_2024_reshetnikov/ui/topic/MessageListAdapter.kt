package ru.elnorte.tfs_spring_2024_reshetnikov.ui.topic

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.elnorte.tfs_spring_2024_reshetnikov.databinding.MessageItemCardBinding
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.MessageUiModel

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
            with(binding.message) {
                setAvatar(item.senderAvatar)
                setName(item.userName)
                setMessageText(item.message)
                setOnLongClickListener {
                    if (item.reactions.isEmpty()) {
                        clickListener.onLongClick(item)
                    }
                    false
                }
                addEmojis(item.reactions, item.checkedReaction)
                setOnEmojiClickListener {
                    clickListener.onEmojiClick(
                        it,
                        item.messageId,
                        item.checkedReaction.contains(it),
                    )
                }
                setOnAddReactionClickListener { clickListener.onAddReactionClick(item.messageId) }
                tuneState(item.isMineMessage)
            }

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
    val emojiClickListener: (emoji: String, messageId: Int, isSelected: Boolean) -> Unit,
    val addReaction: (messageId: Int) -> Unit
) {
    fun onLongClick(item: MessageUiModel): Boolean {
        longClickListener(item.messageId)
        return true
    }

    fun onEmojiClick(item: String, messageId: Int, isSelected: Boolean) {
        emojiClickListener(item, messageId, isSelected)
    }

    fun onAddReactionClick(messageId: Int) {
        addReaction(messageId)
    }
}
