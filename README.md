# Chronos (Formerly Countdowns)

*An app that lets you keep track of countdowns to events.*

## Project Set-up

This project uses Firebase for some functionality. Follow the instructions
[here](https://firebase.google.com/docs/android/setup#console) to set up a new
Firebase project and download a `google-services.json` config file. This must be
placed in the :app:mobile and :app:wear modules. **The respective apps will not
build without this file.**

### Auth

Ensure that Google Sign-in is enabled in the Firebase Console.

## Modules Overview

The project is divided into several modules:

- `:app:mobile` - Android app module for phone devices.
- `:app:wear` - Android app module for wearable devices.
- `:build:logic:convention` - Conventions plugins for managing build configurations.
- `:core:testing` - Android library containing testing utilities.
- `:core:ui` - Android library with common Jetpack Compose UI widgets.
- `:core:coroutines` - Android library with Kotlin coroutine-related utility classes.
- `:core:model` - App-wide data models.
- `:core:util` - Kotlin-only module containing utility functions (not an Android library).
- `:data` - Android library for the data layer.
- `:dynamic` - Dynamic delivery module
- `:feature:details` - Android library for the details feature.
- `:feature:home` - Android library for main app functionality.
- `:feature:search` - Android library for the app-wide search.
- `:feature:widgets` - Android library for home screen widgets.
- `:feature:wear:home` - Android library for the Wear OS standalone app main functionality.
- `:feature:wear:tiles` - Android library for Wear OS tiles.
- `:feature:wear:complications` - Android library Wear OS complications.
- `:test:navigation` - Test-only module for navigation testing.
