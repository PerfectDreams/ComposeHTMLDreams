package org.jetbrains.compose.web.dom

import web.dom.Node

// From Kotlin/JS vanilla bindings
/** Removes all the children from this node. */
@SinceKotlin("1.4")
public fun Node.clear() {
    while (hasChildNodes()) {
        removeChild(firstChild!!)
    }
}