package com.pandaways.chatz.ui.model

/**
 * Created by meyta.taliti on 01/07/23.
 */
data class ChatUiModel(
    val messages: List<Message>,
    val sender: Author,
) {
    data class Message(
        val text: String,
        val author: Author,
    )

    data class Author(
        val id: String,
        val name: String
    ) {
        val isFromMe: Boolean
            get() = id == MY_ID
    }

    companion object {
        const val MY_ID = "-1"
    }
}