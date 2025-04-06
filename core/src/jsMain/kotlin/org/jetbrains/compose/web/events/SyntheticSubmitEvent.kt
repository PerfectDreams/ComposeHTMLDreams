package org.jetbrains.compose.web.events

import androidx.compose.web.events.SyntheticEvent
import web.events.Event
import web.events.EventTarget

class SyntheticSubmitEvent internal constructor(
    nativeEvent: Event
) : SyntheticEvent<EventTarget>(nativeEvent)
