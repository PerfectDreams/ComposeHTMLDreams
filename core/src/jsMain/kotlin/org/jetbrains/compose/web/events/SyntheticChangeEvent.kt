package org.jetbrains.compose.web.events

import androidx.compose.web.events.SyntheticEvent
import web.events.Event
import web.events.EventTarget

class SyntheticChangeEvent<Value, Element : EventTarget> internal constructor(
    val value: Value,
    nativeEvent: Event,
) : SyntheticEvent<Element>(nativeEvent)
