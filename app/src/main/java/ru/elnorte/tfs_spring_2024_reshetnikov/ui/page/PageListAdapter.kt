package ru.elnorte.tfs_spring_2024_reshetnikov.ui.page

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.elnorte.tfs_spring_2024_reshetnikov.databinding.ChannelItemCardBinding
import ru.elnorte.tfs_spring_2024_reshetnikov.databinding.TopicItemCardBinding
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.ChannelUiModel
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.TopicUiModel
import ru.elnorte.tfs_spring_2024_reshetnikov.utils.PAGE_ITEM_TYPE_CHANNEL
import ru.elnorte.tfs_spring_2024_reshetnikov.utils.PAGE_ITEM_TYPE_TOPIC

class PageListAdapter(private val clickListener: PageClickListener) :
    ListAdapter<PageItem, RecyclerView.ViewHolder>(PageDiffCallBack()) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ChannelViewHolder -> {
                val channelItem = getItem(position) as PageItem.ChannelItem
                holder.bind(channelItem.channel)
            }

            is TopicViewHolder -> {
                val channelItem = getItem(position) as PageItem.TopicItem
                holder.bind(channelItem.topic)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            PAGE_ITEM_TYPE_CHANNEL -> ChannelViewHolder.from(parent, clickListener)
            PAGE_ITEM_TYPE_TOPIC -> TopicViewHolder.from(parent, clickListener)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is PageItem.ChannelItem -> PAGE_ITEM_TYPE_CHANNEL
            is PageItem.TopicItem -> PAGE_ITEM_TYPE_TOPIC
        }
    }

    class ChannelViewHolder private constructor(
        private val binding: ChannelItemCardBinding,
        private val clickListener: PageClickListener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ChannelUiModel) {
            binding.channelName.text = item.name
            if (item.isExpanded) {
                binding.chevronImage.rotation = 180.0f
            } else {
                binding.chevronImage.rotation = 0.0f
            }
            binding.channelItemCard.setOnClickListener {
                clickListener.onChannelClick(item.channelId)
            }
        }

        companion object {
            fun from(parent: ViewGroup, clickListener: PageClickListener): ChannelViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ChannelItemCardBinding.inflate(layoutInflater, parent, false)
                return ChannelViewHolder(binding, clickListener)
            }
        }
    }

    class TopicViewHolder private constructor(
        private val binding: TopicItemCardBinding,
        private val clickListener: PageClickListener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TopicUiModel) {
            binding.topicName.text = item.name
            binding.topicMessageCount.text = item.messages
            binding.topicItemCard.setBackgroundColor(Color.parseColor(item.color))
            binding.topicItemCard.setOnClickListener {
                clickListener.onTopicClick(item.name, item.parentId)
            }
        }

        companion object {
            fun from(parent: ViewGroup, clickListener: PageClickListener): TopicViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = TopicItemCardBinding.inflate(layoutInflater, parent, false)
                return TopicViewHolder(binding, clickListener)
            }
        }
    }
}

class PageDiffCallBack : DiffUtil.ItemCallback<PageItem>() {

    override fun areItemsTheSame(oldItem: PageItem, newItem: PageItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: PageItem, newItem: PageItem): Boolean {
        return oldItem == newItem
    }
}


class PageClickListener(
    val channelClickListener: (item: Int) -> Unit,
    val topicClickListener: (item: String, parent: Int) -> Unit
) {

    fun onChannelClick(item: Int) = channelClickListener(item)
    fun onTopicClick(item: String, parent: Int) = topicClickListener(item, parent)
}
