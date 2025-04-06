import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.renderComposable
import web.dom.document

fun main() {
    val root = document.getElementById("root")!!

    // It could also be "root", but we use the root element here because we wanted to show off the browser bindings element
    renderComposable(root) {
        var counter by remember { mutableStateOf(0) }

        Div {
            Button(attrs = {
                onClick {
                    counter++
                }
            }) {
                Text("Click Me!")
            }

            Text("Count: $counter")
        }

        Div {
            Button(attrs = {
                ref {
                    // Because we use the new browser bindings elements, the "HTMLButtonElement" here uses the new bindings
                    println("Button Type Here: ${it.type}")
                    onDispose {}
                }

                onClick {
                    // The it.nativeEvent here gets the new bindings too
                    println("Clicked! ${it.type} - ${it.nativeEvent}")
                }
            }) {
                Text("Click to Log")
            }
        }

    }
}