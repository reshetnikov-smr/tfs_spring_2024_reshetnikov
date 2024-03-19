package ru.elnorte.tfs_spring_2024_reshetnikov

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import ru.elnorte.tfs_spring_2024_reshetnikov.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        initBindings()
        setContentView(binding.root)
    }

    private fun initBindings() {
        binding.emptyAvatarButton.setOnClickListener {
            binding.messageBox.setAvatar(null)
        }
        binding.tinyAvatarButton.setOnClickListener {
            binding.messageBox.setAvatar(
                AppCompatResources.getDrawable(
                    applicationContext,
                    R.drawable.tiny_avatar
                )
            )
        }
        binding.regularAvatarButton.setOnClickListener {
            binding.messageBox.setAvatar(
                AppCompatResources.getDrawable(
                    applicationContext,
                    R.drawable.regular_avatar
                )
            )
        }
        binding.hugeAvatarButton.setOnClickListener {
            binding.messageBox.setAvatar(
                AppCompatResources.getDrawable(
                    applicationContext,
                    R.drawable.huge_avatar
                )
            )
        }
        binding.noMessageButton.setOnClickListener {
            binding.messageBox.setMessageText(null)
        }
        binding.regularMessageButton.setOnClickListener {
            binding.messageBox.setMessageText(getString(R.string.example_regular_message))
        }
        binding.veryLongMessageButton.setOnClickListener {
            binding.messageBox.setMessageText(getString(R.string.example_long_message))
        }
        binding.noNameButton.setOnClickListener {
            binding.messageBox.setName(null)
        }
        binding.regularNameButton.setOnClickListener {
            binding.messageBox.setName(getString(R.string.example_regular_name))
        }
        binding.veryLongNameButton.setOnClickListener {
            binding.messageBox.setName(getString(R.string.example_long_name))
        }
        binding.addRandomEmojiButton.setOnClickListener {
            addRandomEmojiToMessage()
        }
    }

    private fun addRandomEmojiToMessage() {
        val emojis = arrayListOf(
            "\uD83D\uDE03",
            "\uD83D\uDE43",
            "\uD83D\uDE0D",
            "\uD83E\uDD2A",
            "\uD83D\uDE12",
            "\uD83D\uDE0E",
            "\uD83E\uDD2E",
            "\uD83D\uDCA9",
            "\uD83E\uDDE1",
            "\uD83E\uDD1E",
            "\uD83D\uDC4D",
            "\uD83D\uDC4E",
            "\uD83D\uDC31",
            "\uD83E\uDD21",
            "\uD83D\uDC40",
        )
        binding.messageBox.addEmoji(emojis.random(), (1..99).random())
    }
}
