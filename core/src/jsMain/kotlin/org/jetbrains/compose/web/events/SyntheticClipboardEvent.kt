package org.jetbrains.compose.web.events

import androidx.compose.web.events.SyntheticEvent
import web.clipboard.ClipboardEvent
import web.data.DataTransfer
import web.events.EventTarget

class SyntheticClipboardEvent internal constructor(
    nativeEvent: ClipboardEvent
) : SyntheticEvent<EventTarget>(nativeEvent) {

    val clipboardData: DataTransfer? = nativeEvent.clipboardData

    fun getData(format: String): String? {
        return clipboardData?.getData(format)
    }

    fun setData(format: String, data: String) {
        clipboardData?.setData(format, data)
    }
}
