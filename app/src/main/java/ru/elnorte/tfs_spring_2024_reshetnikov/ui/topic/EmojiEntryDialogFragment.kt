package ru.elnorte.tfs_spring_2024_reshetnikov.ui.topic

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.elnorte.tfs_spring_2024_reshetnikov.R
import ru.elnorte.tfs_spring_2024_reshetnikov.databinding.EmojiEntryBottomSheetLayoutBinding
import ru.elnorte.tfs_spring_2024_reshetnikov.provideEmojisList

class EmojiEntryDialogFragment(var onSelectEmoji: (String) -> Unit) : BottomSheetDialogFragment() {

    private lateinit var binding: EmojiEntryBottomSheetLayoutBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = EmojiEntryBottomSheetLayoutBinding.inflate(inflater, container, false)
        binding.root.setBackgroundColor(resources.getColor(R.color.container, null))

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        populateView(provideEmojisList())
    }

    private fun populateView(emojis: List<String>) {
        emojis.forEach { emoji ->
            val item = TextView(context)
            val mlp =
                ViewGroup.MarginLayoutParams(
                    ViewGroup.MarginLayoutParams.WRAP_CONTENT,
                    ViewGroup.MarginLayoutParams.WRAP_CONTENT
                )
            mlp.bottomMargin =
                TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    5f,
                    resources.displayMetrics
                )
                    .toInt()
            mlp.topMargin =
                TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    5f,
                    resources.displayMetrics
                )
                    .toInt()
            mlp.leftMargin =
                TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    5f,
                    resources.displayMetrics
                )
                    .toInt()
            mlp.rightMargin =
                TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    5f,
                    resources.displayMetrics
                )
                    .toInt()
            item.apply {
                layoutParams = mlp
                textSize = resources.getDimension(R.dimen.font_standard)
                text = emoji
                setOnClickListener {
                    onSelectEmoji(emoji)
                    dismiss()
                }
            }
            binding.bottomSheetMainTable.addView(item)
        }
    }
}
