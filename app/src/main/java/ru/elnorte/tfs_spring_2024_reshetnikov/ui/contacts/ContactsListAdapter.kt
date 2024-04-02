package ru.elnorte.tfs_spring_2024_reshetnikov.ui.contacts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.elnorte.tfs_spring_2024_reshetnikov.databinding.PersonItemCardBinding
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
                personsAvatar.setImageDrawable(
                    item.avatar?.let {
                        AppCompatResources.getDrawable(
                            this.root.context,
                            it
                        )
                    }
                )
                personsAvatar.clipToOutline = true
                personsCard.setOnClickListener {
                    clickListener.onClick(item.personId)
                }
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
    val clickListener: (personId: Int) -> Unit,
) {

    fun onClick(id: Int) {
        clickListener(id)
    }
}
