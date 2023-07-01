package com.pandaways.chatz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.pandaways.chatz.ui.ChatScreen
import com.pandaways.chatz.ui.model.ChatUiModel
import com.pandaways.chatz.ui.theme.ChatzTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChatzTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ChatScreen(
                        model = ChatUiModel(
                            messages = listOf(
                                ChatUiModel.Message(
                                    "Hi Tree, How you doing?",
                                    ChatUiModel.Author("0", "Branch")
                                ),
                                ChatUiModel.Message(
                                    "Hi Branch, good. You?",
                                    ChatUiModel.Author("-1", "Tree"))
                            ),
                            sender = ChatUiModel.Author("0", "Branch")
                        ),
                        modifier = Modifier
                    )
                }
            }
        }
    }
}