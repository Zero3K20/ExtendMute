# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Keep the accessibility service
-keep class com.extendmute.MuteToggleService { *; }
-keep class com.extendmute.MainActivity { *; }
