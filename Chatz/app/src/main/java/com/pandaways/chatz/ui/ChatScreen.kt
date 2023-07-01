package com.pandaways.chatz.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.pandaways.chatz.ui.model.ChatUiModel
import com.pandaways.chatz.ui.theme.ChatzTheme
import com.pandaways.chatz.ui.theme.PurpleGrey80

/**
 * Created by meyta.taliti on 01/07/23.
 */
@Preview(showSystemUi = true)
@Composable
fun ChatScreenPreview() {
    ChatzTheme {
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

@Composable
fun ChatScreen(
    model: ChatUiModel,
    modifier: Modifier
) {
    ConstraintLayout(modifier = modifier
        .fillMaxSize()
        .padding(16.dp)) {
        val (messages, chatBox) = createRefs()

        LazyColumn(modifier = Modifier
            .fillMaxWidth()
            .constrainAs(messages) {
                top.linkTo(parent.top)
                bottom.linkTo(chatBox.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                height = Dimension.fillToConstraints
            }
        ) {
            items(model.messages) { item ->
                ChatItem(item)
            }
        }
        ChatBox(modifier = Modifier
            .fillMaxWidth()
            .constrainAs(chatBox) {
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            })
    }
}

@Composable
fun ChatItem(message: ChatUiModel.Message) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(4.dp)) {
        Box(
            modifier = Modifier
                .align(if (message.author.isFromMe) Alignment.End else Alignment.Start)
                .clip(
                    AbsoluteRoundedCornerShape(
                        topLeftPercent = 50,
                        topRightPercent = 50,
                        bottomLeftPercent = if (message.author.isFromMe) 50 else 0,
                        bottomRightPercent = if (message.author.isFromMe) 0 else 50
                    )
                )
                .background(PurpleGrey80)
                .padding(16.dp)
                .wrapContentSize(
                    if (message.author.isFromMe) Alignment.TopEnd else Alignment.TopEnd
                )
        ) {
            Text(text = message.text)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatBox(modifier: Modifier) {
    var text by remember { mutableStateOf(TextFieldValue("")) }
    Row(modifier = modifier) {
        TextField(
            value = text,
            onValueChange = { newText ->
                text = newText
            },
            modifier = Modifier
                .weight(1f)
                .padding(4.dp),
            shape = RoundedCornerShape(24.dp),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            placeholder = {
                Text(text = "Type something")
            }
        )
        IconButton(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .clip(CircleShape)
                .background(color = PurpleGrey80)
                .align(Alignment.CenterVertically)
        ) {
            Icon(
                imageVector = Icons.Filled.Send,
                contentDescription = "Send",
                modifier = Modifier.fillMaxSize().padding(8.dp)
            )
        }
    }
}