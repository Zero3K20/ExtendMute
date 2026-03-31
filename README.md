# ExtendMute

Makes the mute key on a ChromeBook un-mute when muted — turning it into a true mute/un-mute toggle.

## What it does

On many Chromebooks the physical mute key (or `F8`) mutes audio but pressing it again while already muted does nothing. **ExtendMute** is an Android APK (runs via ChromeOS's built-in Android/ARC++ support) that intercepts that key press and un-mutes the audio stream, so the key behaves as a proper toggle.

## How it works

The app registers an Android **AccessibilityService** (`MuteToggleService`) that requests key-event filtering. When the mute key (`KEYCODE_VOLUME_MUTE`) is pressed:

| Audio state | What happens |
|-------------|--------------|
| Currently **muted** | Service un-mutes the audio and consumes the event (so Chrome OS doesn't re-mute). |
| Currently **unmuted** | Event is passed through normally, letting Chrome OS apply the mute as usual. |

## Building the APK

### Prerequisites
- Android Studio (Hedgehog or newer) **or** JDK 17 + Android SDK

### Steps

```bash
# From the repository root:
./gradlew assembleRelease
# Output: app/build/outputs/apk/release/app-release-unsigned.apk
```

For a debug build (easier to side-load):

```bash
./gradlew assembleDebug
# Output: app/build/outputs/apk/debug/app-debug.apk
```

### Installing on ChromeBook

1. Enable **Linux (Beta)** or **Android apps** in ChromeOS Settings.
2. Turn on **Developer options** inside Android Settings → About → tap "Build number" 7 times.
3. Enable **Unknown sources** (or use `adb install`):
   ```bash
   adb install app/build/outputs/apk/debug/app-debug.apk
   ```

## First-time setup

1. Open the **ExtendMute** app from the ChromeOS launcher.
2. Tap **Open Accessibility Settings**.
3. Find **ExtendMute Mute Toggle** and enable it.
4. Return to the app — the status indicator should turn green.

The service runs in the background; no further interaction is needed. Press the mute key to mute, press it again to un-mute.

## Permissions

| Permission | Reason |
|------------|--------|
| `MODIFY_AUDIO_SETTINGS` | Required to un-mute the audio stream. |
| `BIND_ACCESSIBILITY_SERVICE` | Required to receive and filter hardware key events. |
