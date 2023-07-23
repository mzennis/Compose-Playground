package com.pandaways.chatz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.pandaways.chatz.ui.ChatScreen
import com.pandaways.chatz.ui.MainViewModel
import com.pandaways.chatz.ui.model.ChatUiModel
import com.pandaways.chatz.ui.theme.ChatzTheme

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val conversation = viewModel.conversation.collectAsState()

            ChatzTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ChatScreen(
                        model = ChatUiModel(
                            messages = conversation.value,
                            addressee = ChatUiModel.Author.bot
                        ),
                        onSendChatClickListener = { msg -> viewModel.sendChat(msg) },
                        modifier = Modifier
                    )
                }
            }
        }
    }
}