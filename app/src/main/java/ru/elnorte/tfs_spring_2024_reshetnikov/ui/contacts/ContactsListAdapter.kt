package ru.elnorte.tfs_spring_2024_reshetnikov.ui.contacts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import ru.elnorte.tfs_spring_2024_reshetnikov.R
import ru.elnorte.tfs_spring_2024_reshetnikov.databinding.PersonItemCardBinding
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.OnlineStatus
import ru.elnorte.tfs_spring_2024_reshetnikov.ui.models.PersonUiModel

class ContactsListAdapter(private val clickListener: ContactsClickListener) :
    ListAdapter<PersonUiModel, ContactsListAdapter.ViewHolder>(ContactsDiffCallBack()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(private val binding: PersonItemCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PersonUiModel, clickListener: ContactsClickListener) {
            with(binding) {
                personsName.text = item.name
                personsEmail.text = item.email
                onlineIndicator.drawable.setTint(
                    when (item.isOnline) {
                        OnlineStatus.Online -> binding.root.context.getColor(R.color.online)
                        OnlineStatus.Idle -> binding.root.context.getColor(R.color.idle)
                        OnlineStatus.Offline -> binding.root.context.getColor(R.color.offline)
                    }
                )
                personsAvatar.clipToOutline = true
                personsCard.setOnClickListener {
                    clickListener.onClick(item)
                }
            }
            if (item.avatar != null) {
                val imgUri = item.avatar.toUri().buildUpon().scheme("https").build()
                Glide.with(binding.personsAvatar.context)
                    .load(imgUri)
                    .apply(
                        RequestOptions()
                            .placeholder(R.drawable.loading_animation)
                            .error(R.drawable.download_fail_image)
                    )
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(binding.personsAvatar)
            } else {
                binding.personsAvatar.setImageResource(R.drawable.stub_avatar)
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = PersonItemCardBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class ContactsDiffCallBack : DiffUtil.ItemCallback<PersonUiModel>() {

    override fun areItemsTheSame(oldItem: PersonUiModel, newItem: PersonUiModel): Boolean {
        return oldItem.personId == newItem.personId
    }

    override fun areContentsTheSame(oldItem: PersonUiModel, newItem: PersonUiModel): Boolean {
        return oldItem == newItem
    }
}

class ContactsClickListener(
    val clickListener: (personId: PersonUiModel) -> Unit,
) {

    fun onClick(id: PersonUiModel) {
        clickListener(id)
    }
}
