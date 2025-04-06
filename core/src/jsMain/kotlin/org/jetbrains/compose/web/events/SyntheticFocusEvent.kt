package org.jetbrains.compose.web.events

import androidx.compose.web.events.SyntheticEvent
import web.events.EventTarget
import web.uievents.FocusEvent

class SyntheticFocusEvent internal constructor(
    nativeEvent: FocusEvent,
) : SyntheticEvent<EventTarget>(nativeEvent) {

    val relatedTarget: EventTarget? = nativeEvent.relatedTarget
}
