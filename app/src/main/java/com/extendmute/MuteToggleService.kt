package com.extendmute

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.media.AudioManager
import android.view.KeyEvent
import android.view.accessibility.AccessibilityEvent

/**
 * An AccessibilityService that intercepts the mute key on ChromeBook.
 *
 * When the mute key (KEYCODE_VOLUME_MUTE) is pressed while audio is already
 * muted, this service un-mutes the audio so the mute button acts as a true
 * toggle rather than a one-way mute-only key.
 */
class MuteToggleService : AccessibilityService() {

    private lateinit var audioManager: AudioManager

    override fun onServiceConnected() {
        audioManager = getSystemService(AUDIO_SERVICE) as AudioManager

        serviceInfo = serviceInfo.apply {
            flags = flags or AccessibilityServiceInfo.FLAG_REQUEST_FILTER_KEY_EVENTS
        }
    }

    override fun onKeyEvent(event: KeyEvent): Boolean {
        // Only act on the mute key on the key-down action to avoid double-firing.
        if (event.keyCode != KeyEvent.KEYCODE_VOLUME_MUTE ||
            event.action != KeyEvent.ACTION_DOWN
        ) {
            return false
        }

        // isStreamMute correctly distinguishes between volume=0 and the mute flag,
        // which is exactly what we need here (a stream can be muted while its volume > 0).
        val musicMuted = audioManager.isStreamMute(AudioManager.STREAM_MUSIC)
        return if (musicMuted) {
            // Audio is muted — un-mute it and consume the event so the system
            // does not re-mute it in response to the same key press.
            audioManager.adjustStreamVolume(
                AudioManager.STREAM_MUSIC,
                AudioManager.ADJUST_UNMUTE,
                AudioManager.FLAG_SHOW_UI
            )
            true
        } else {
            // Audio is not muted — let the system handle the key press normally
            // so the mute key continues to mute as expected.
            false
        }
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        // No accessibility events needed; key interception is handled in onKeyEvent.
    }

    override fun onInterrupt() {
        // Nothing to clean up.
    }
}
