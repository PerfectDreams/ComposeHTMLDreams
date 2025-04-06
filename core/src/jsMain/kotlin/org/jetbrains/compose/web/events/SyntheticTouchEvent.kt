package org.jetbrains.compose.web.events

import androidx.compose.web.events.SyntheticEvent
import web.events.EventTarget
import web.uievents.TouchEvent
import web.uievents.TouchList

class SyntheticTouchEvent(
    nativeEvent: TouchEvent,
) : SyntheticEvent<EventTarget>(nativeEvent) {

    val altKey: Boolean = nativeEvent.altKey
    val changedTouches: TouchList = nativeEvent.changedTouches
    val ctrlKey: Boolean = nativeEvent.ctrlKey
    val metaKey: Boolean = nativeEvent.metaKey
    val shiftKey: Boolean = nativeEvent.shiftKey
    val touches: TouchList = nativeEvent.touches
}
