package ru.elnorte.tfs_spring_2024_reshetnikov.ui.topic

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import ru.elnorte.tfs_spring_2024_reshetnikov.R


class MessageViewGroup @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0,
    defTheme: Int = 0,
) : ConstraintLayout(context, attributeSet, defStyle, defTheme) {

    private lateinit var onEmojiClick: (String) -> Unit
    private lateinit var onAddReactionClick: () -> Unit

    fun hideAvatar(isHidden: Boolean) {
        if (isHidden) {
            findViewById<ImageView>(R.id.avatarImageView).visibility = View.GONE
        } else {
            findViewById<ImageView>(R.id.avatarImageView).visibility = View.VISIBLE
        }
    }

    init {
        inflate(context, R.layout.message_viewgroup_layout, this)
    }

    private fun addEmoji(emoji: String, count: Int, checked: Boolean) {
        val box = findViewById<FlexBoxLayout>(R.id.reactionFlexBox)
        val mlp =
            MarginLayoutParams(MarginLayoutParams.WRAP_CONTENT, MarginLayoutParams.WRAP_CONTENT)
        mlp.bottomMargin =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5f, resources.displayMetrics)
                .toInt()
        mlp.topMargin =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5f, resources.displayMetrics)
                .toInt()
        mlp.leftMargin =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5f, resources.displayMetrics)
                .toInt()
        mlp.rightMargin =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5f, resources.displayMetrics)
                .toInt()
        val emo = EmojiView(this.context)
        emo.emoji = emoji
        emo.count = count
        emo.background = AppCompatResources.getDrawable(context, R.drawable.emoji_bg)
        emo.layoutParams = mlp
        emo.setPadding(
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 9f, resources.displayMetrics
            ).toInt(),
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5f, resources.displayMetrics)
                .toInt(),
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 9f, resources.displayMetrics)
                .toInt(),
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5f, resources.displayMetrics)
                .toInt()
        )
        if (checked) {
            emo.isSelected = true
        }
        emo.setOnClickListener {
            onEmojiClick.invoke(emoji)
        }
        box.addView(emo)
    }

    private fun clearEmojis() {
        findViewById<FlexBoxLayout>(R.id.reactionFlexBox).removeAllViews()
    }

    fun addEmojis(emojis: Map<String, Int>?, selectedEmoji: String?) {
        clearEmojis()
        emojis?.forEach {
            addEmoji(it.key, it.value, it.key == selectedEmoji)
        }
        if (!emojis.isNullOrEmpty()) {
            initializeFlexBox()
        }

    }

    fun setName(name: String?) {
        val nameString = findViewById<TextView>(R.id.senderName)
        nameString.text = name ?: context.getText(R.string.missing_name)
    }

    fun setMessageText(name: String?) {
        val messageString = findViewById<TextView>(R.id.messageText)
        messageString.text = name ?: context.getText(R.string.missing_message)
    }

    fun setAvatar(avatar: Drawable?) {
        val avatarImage = findViewById<ImageView>(R.id.avatarImageView)
        avatarImage.setImageDrawable(
            avatar ?: AppCompatResources.getDrawable(
                context, R.drawable.stub_avatar
            )
        )
        avatarImage.clipToOutline = true
    }

    fun setOnEmojiClickListener(lambda: (String) -> Unit) {
        onEmojiClick = lambda
    }

    fun setOnAddReactionClickListener(lambda: () -> Unit) {
        onAddReactionClick = lambda
    }

    private fun initializeFlexBox() {
        val box = findViewById<FlexBoxLayout>(R.id.reactionFlexBox)
        val width =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 45f, resources.displayMetrics)
                .toInt()
        val height =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30f, resources.displayMetrics)
                .toInt()
        val mlp = MarginLayoutParams(width, height)
        mlp.bottomMargin =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5f, resources.displayMetrics)
                .toInt()
        mlp.topMargin =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5f, resources.displayMetrics)
                .toInt()
        mlp.leftMargin =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5f, resources.displayMetrics)
                .toInt()
        mlp.rightMargin =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5f, resources.displayMetrics)
                .toInt()
        val image = ImageView(context)
        image.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.plus_sign))
        image.background = AppCompatResources.getDrawable(context, R.drawable.add_emoji_bg)
        image.layoutParams = mlp
        image.setOnClickListener {
            onAddReactionClick.invoke()
        }
        box.addView(image)
    }
}
