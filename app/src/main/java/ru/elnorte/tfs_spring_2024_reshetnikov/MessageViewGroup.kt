package ru.elnorte.tfs_spring_2024_reshetnikov

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.Snackbar

class MessageViewGroup @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0,
    defTheme: Int = 0,
) : ConstraintLayout(context, attributeSet, defStyle, defTheme) {

    init {
        inflate(context, R.layout.message_viewgroup_layout, this)
        initializeFlexBox()
    }

    fun addEmoji(emoji: String, count: Int) {
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
        emo.setOnClickListener {
            if (it.isSelected) {
                (it as EmojiView).count--
            } else {
                (it as EmojiView).count++
            }
            it.isSelected = !it.isSelected
        }
        box.addView(emo, box.childCount - 1)
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
            Snackbar.make(it, context.getString(R.string.wip_stub), Snackbar.LENGTH_SHORT).show()
        }
        box.addView(image)
    }
}
