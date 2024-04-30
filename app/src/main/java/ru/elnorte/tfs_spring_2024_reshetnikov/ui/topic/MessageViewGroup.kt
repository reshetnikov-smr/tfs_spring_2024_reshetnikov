package ru.elnorte.tfs_spring_2024_reshetnikov.ui.topic

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import ru.elnorte.tfs_spring_2024_reshetnikov.R
import ru.elnorte.tfs_spring_2024_reshetnikov.dp


class MessageViewGroup @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0,
    defTheme: Int = 0,
) : ConstraintLayout(context, attributeSet, defStyle, defTheme) {

    private lateinit var onEmojiClick: (String) -> Unit
    private lateinit var onAddReactionClick: () -> Unit

    private var avatarView: ImageView
    private var messageCard: CardView
    private var flexBoxLayout: FlexBoxLayout
    private var senderName: TextView

    fun tuneState(isMe: Boolean) {
        if (isMe) {
            avatarView.visibility = View.GONE
            senderName.visibility = View.GONE
            val params = messageCard.layoutParams as LayoutParams
            params.marginStart = 80.dp
            params.marginEnd = 8.dp
            messageCard.layoutParams = params
            meBackgroundColor()
        } else {
            avatarView.visibility = View.VISIBLE
            senderName.visibility = View.VISIBLE
            val params = messageCard.layoutParams as LayoutParams
            params.marginStart = 8.dp
            params.marginEnd = 60.dp
            messageCard.layoutParams = params
            otherBackgroundColor()
        }
    }

    private fun meBackgroundColor() {
        val gradientDrawable =
            ContextCompat.getDrawable(context, R.drawable.me_message_background)
        messageCard.background = gradientDrawable
    }

    private fun otherBackgroundColor() {
        messageCard.background = ContextCompat.getDrawable(context, R.drawable.message_bg)
    }


    init {
        inflate(context, R.layout.message_viewgroup_layout, this)
        avatarView = findViewById(R.id.avatarImageView)
        messageCard = findViewById(R.id.messageCard)
        flexBoxLayout = findViewById(R.id.reactionFlexBox)
        senderName = findViewById(R.id.senderName)
    }

    private fun addEmoji(emoji: String, count: Int, checked: Boolean) {
        val box = findViewById<FlexBoxLayout>(R.id.reactionFlexBox)
        val mlp =
            MarginLayoutParams(MarginLayoutParams.WRAP_CONTENT, MarginLayoutParams.WRAP_CONTENT)
        mlp.bottomMargin = marginToPx(5f)
        mlp.topMargin = marginToPx(5f)
        mlp.leftMargin = marginToPx(5f)
        mlp.rightMargin = marginToPx(5f)
        val emo = EmojiView(this.context)
        emo.emoji = emoji
        emo.count = count
        emo.background = AppCompatResources.getDrawable(context, R.drawable.emoji_bg)
        emo.layoutParams = mlp
        emo.setPadding(
            marginToPx(9f),
            marginToPx(5f),
            marginToPx(9f),
            marginToPx(5f)
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

    fun addEmojis(emojis: Map<String, Int>?, selectedEmoji: Set<String>) {
        clearEmojis()
        emojis?.forEach {
            addEmoji(it.key, it.value, selectedEmoji.contains(it.key))
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

    fun setAvatar(avatarUrl: String?) {
        Glide.with(this)
            .load(avatarUrl)
            .placeholder(R.drawable.stub_avatar)
            .error(R.drawable.download_fail_image)
            .into(avatarView)
        avatarView.clipToOutline = true
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
            marginToPx(45f)
        val height =
            marginToPx(30f)
        val mlp = MarginLayoutParams(width, height)
        mlp.bottomMargin = marginToPx(5f)
        mlp.topMargin =
            marginToPx(5f)
        mlp.leftMargin =
            marginToPx(5f)
        mlp.rightMargin =
            marginToPx(5f)
        val image = ImageView(context)
        image.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.plus_sign))
        image.background = AppCompatResources.getDrawable(context, R.drawable.add_emoji_bg)
        image.layoutParams = mlp
        image.setOnClickListener {
            onAddReactionClick.invoke()
        }
        box.addView(image)
    }

    private fun marginToPx(margin: Float) =
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            margin,
            resources.displayMetrics
        ).toInt()
}
