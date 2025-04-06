# Compose HTML Fork

A Compose HTML fork targeting the [modernized Kotlin Browser bindings](https://github.com/JetBrains/kotlin-wrappers/tree/master/kotlin-browser) (very experimental and mostly meant to see how Compose HTML works behind the hood).

Should you use this? Probably not.

## What is Compose HTML?

Compose HTML is a library targeting [Kotlin/JS](https://kotlinlang.org/docs/js-overview.html) that provides Composable building blocks for creating web user interfaces with HTML and CSS. The upstream version is present on the [Compose Multiplatform repository](https://github.com/JetBrains/compose-multiplatform/tree/master/html).

## Why fork Compose HTML?

JetBrains is not updating Compose HTML because they are focusing on Compose Web, which is a version of Compose that is *actually* multiplatform that targets Kotlin/WASM and the Canvas, letting you share components between Desktop, Android, Web, and other platforms supported by Compose.

Compose HTML still works fine, but because its future is still uncertain, I've decided to fork it and find out if maintaining Compose HTML would be hard.

After looking into Compose HTML's code, the code is not actually *that* complex. Of course, most of the heavy lifting is done by Jetpack Compose.

For this fork, I've changed (most) of the code to use the [modernized Kotlin Browser bindings](https://github.com/JetBrains/kotlin-wrappers/tree/master/kotlin-browser) (the module's README says that they are only "Browser APIs missing from the standard library" but as far as I know they also have all the APIs included in Kotlin/JS too, but the quality of the `kotlin-browser` bindings are better than the bindings included in Kotlin/JS)

But at the end of the day, using the modernized Kotlin Browser bindings in Compose HTML is not *that* useful, the only difference you would see when using Compose HTML is that the `renderComposable` function requires a `web.dom.Element` instead of a `org.w3c.dom.Element`, that you have access to better event bindings when accessing the `nativeEvent` of a synthetic event, and that you have the `web.html` bindings when using `ref { ... }`.

## Why not use React instead of Compose HTML?

While the Compose Multiplatform ecosystem competes against frameworks that let you create apps for multiple platforms (like React Native and Flutter), Compose HTML competition is React.

You can use React in Kotlin/JS by using the [React Bindings](https://github.com/JetBrains/kotlin-wrappers/tree/master/kotlin-react-dom), and it does work fine!

However, in my experience, Compose HTML is easier to use than React. You can easily integrate things outside the Compose tree, whereas with React you need to create a subscription system to update the state inside the tree. You also don't need to declare props in an interface.

## Using Compose HTML Dreams in a Project

I don't recommend using this fork in your projects, because this fork is VERY experimental. But if you still want to go through it...

Add `maven("https://repo.perfectdreams.net/")` to your repositories.

Replace `implementation(compose.html.core)` with `implementation("net.perfectdreams.compose.htmldreams:html-core:1.7.3")` in your `build.gradle.kts`.

If you are still using the original Kotlin Browser bindings (`org.w3c`), you may encounter a lot of errors in a project that already uses Compose HTML. You can work around a lot of these errors by `unsafeCast`'ing to the new `web.html` bindings (`w3cElement.unsafeCast<web.html.HTMLElement>()`), and then migrate your codebase to `web.html` over time.

## Prerequisites for running Selenium tests

As of now Selenium tests are turned on by default.
The minimal requirement for running this tests is to have Chrome
and chromedriver installed on your machine. Currently, we
don't install Chrome via gradle scripts, so if you are running
tests locally **make sure you have Chrome installed**.

For installing chrome driver just run following command:
```kotlin
./gradlew installWebDrivers
```
