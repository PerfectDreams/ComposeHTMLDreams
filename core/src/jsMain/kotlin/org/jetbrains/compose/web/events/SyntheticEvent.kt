package androidx.compose.web.events

import web.events.Event
import web.events.EventPhase
import web.events.EventTarget
import web.events.EventType

open class SyntheticEvent<Element : EventTarget> internal constructor(
    val nativeEvent: Event
) {
    val target: Element = nativeEvent.target.unsafeCast<Element>()
    val bubbles: Boolean = nativeEvent.bubbles
    val cancelable: Boolean = nativeEvent.cancelable
    val composed: Boolean = nativeEvent.composed
    val currentTarget: EventTarget? = nativeEvent.currentTarget
    val eventPhase: EventPhase = nativeEvent.eventPhase
    val defaultPrevented: Boolean = nativeEvent.defaultPrevented
    val timestamp: Number = nativeEvent.timeStamp
    val type: EventType<Event> = nativeEvent.type
    val isTrusted: Boolean = nativeEvent.isTrusted

    fun preventDefault(): Unit = nativeEvent.preventDefault()
    fun stopPropagation(): Unit = nativeEvent.stopPropagation()
    fun stopImmediatePropagation(): Unit = nativeEvent.stopImmediatePropagation()
    fun composedPath(): Array<out EventTarget> = nativeEvent.composedPath()
}
