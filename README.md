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

#### Option A — Via the ChromeOS Files app (no Linux required)

1. Enable **Android apps** in ChromeOS Settings if not already on.
2. Download or copy the APK to your Chromebook's **Downloads** folder.
3. Open the **Files** app and tap the APK; ChromeOS will prompt you to install it.
4. If prompted, allow installation from unknown sources.

#### Option B — Via `adb` from the ChromeOS Linux development environment (Crostini)

When you use `adb` from inside the Linux container you must first connect it to
the ChromeOS Android (ARC++) subsystem, which listens on `localhost:5555`.

1. In **ChromeOS Settings → Advanced → Developers**, turn on **Linux development
   environment** (if not already enabled).
2. In the same **Developers** section (or inside **Linux settings**), enable
   **ADB debugging**.  ChromeOS will display a confirmation dialog the first
   time you connect.
3. Open a **Linux terminal** and install the `adb` client if needed:
   ```bash
   sudo apt-get update && sudo apt-get install -y adb
   ```
4. Connect ADB to the ChromeOS Android container:
   ```bash
   adb connect localhost:5555
   ```
   You should see `connected to localhost:5555` (or `already connected`).
   Accept the RSA-key dialog that pops up on screen if this is your first time.
5. Install the APK:
   ```bash
   adb install app/build/outputs/apk/debug/app-debug.apk
   ```
   A successful install prints `Success` on the last line.

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
